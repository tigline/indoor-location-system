package com.lgguan.iot.position.service

import cn.hutool.core.date.DateUtil
import com.fasterxml.jackson.databind.JsonNode
import com.lgguan.iot.position.entity.AoaDataInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import com.lgguan.iot.position.entity.BeaconInfo
import com.lgguan.iot.position.entity.GatewayInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.lgguan.iot.position.bean.IotBeaconData
import com.lgguan.iot.position.bean.MessageType
import com.lgguan.iot.position.bean.WsMessage
import com.lgguan.iot.position.external.ExternalFenceHandler
import com.lgguan.iot.position.util.Point
import com.lgguan.iot.position.ws.sendWsMessage
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import java.util.*

private class UpdateMessage(val type: String, val data: JsonNode)
@Service
class GatewayAndBeaconService {

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, Any>
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var aoaDataTask: TaskQueueService

    @PostConstruct
    fun init() {
        listenForUpdates()
        aoaDataTask.setGatewayAndBeaconService(this)
    }

    private fun deserialize(data: ByteArray): UpdateMessage {
        return objectMapper.readValue(data, UpdateMessage::class.java)
    }

    fun handleAoaData(aoaData: IotBeaconData, externalFenceHandler: ExternalFenceHandler) {

        val gatewayInfo: GatewayInfo = getGatewayInfoFromRedis(aoaData.gateway ?: "")

        val beaconInfo: BeaconInfo? = getBeaconInfoFromRedis(aoaData.beaconId ?: "")

        if (beaconInfo != null) {
            val prevPoint = Point((beaconInfo.posX ?: 0f) as Double, (beaconInfo.posY ?: 0f) as Double)
            val aoaDataInfo = getAoaDataInfo(aoaData)
            aoaDataInfo.mapId = gatewayInfo?.mapId

            aoaDataInfo.type = beaconInfo?.type

            beaconInfo.apply {
                mac = aoaData.mac
                gateway = aoaData.gateway
                mapId = gatewayInfo?.mapId ?: mapId
                systemId = aoaData.systemId
                motion = aoaData.motion
                //optScale = aoaData.optScale
                positionType = aoaData.positionType
                posX = aoaData.posX
                posY = aoaData.posY
                online = true
                updateTime = aoaDataInfo.timestamp
            }

            CoroutineScope(Dispatchers.IO).launch {
                //log.info("Received AoaDataInfo message: ${aoaDataInfo}")
                sendWsMessage(WsMessage(MessageType.AOAData, aoaDataInfo))
                if ("freezing" != beaconInfo.motion) {
                    externalFenceHandler.emit(beaconInfo to prevPoint)
                }
            }

            updateRedisBeaconInfo(beaconInfo)
            insertRedisAoaDataInfo(aoaDataInfo)

        }

    }

    private fun getAoaDataInfo(iotBeaconData: IotBeaconData): AoaDataInfo {
        val aoaDataInfo = AoaDataInfo()
        aoaDataInfo.deviceId = iotBeaconData.beaconId
        aoaDataInfo.gatewayId = iotBeaconData.gateway
        aoaDataInfo.posX = iotBeaconData.posX
        aoaDataInfo.posY = iotBeaconData.posY
        aoaDataInfo.posZ = iotBeaconData.posZ
        aoaDataInfo.timestamp = if(Objects.nonNull(iotBeaconData.time)) iotBeaconData.time else DateUtil.currentSeconds()
        aoaDataInfo.mapId = iotBeaconData.mapId
        aoaDataInfo.zoneId = iotBeaconData.zoneId
        aoaDataInfo.status = if(iotBeaconData.motion == "freezing") 0 else 1
        aoaDataInfo.createTime = Date()
        return aoaDataInfo
    }



    fun updateRedisBeaconInfo(beaconInfo: BeaconInfo) {
        // Save updated BeaconInfo to Redis.
        redisTemplate.opsForValue().set("beacon:${beaconInfo.deviceId}", beaconInfo)

        // Publish a message about the updated BeaconInfo.
        val message = UpdateMessage("BeaconInfo", objectMapper.valueToTree(beaconInfo))
        redisTemplate.convertAndSend("updates", message)
    }

    fun insertRedisAoaDataInfo(aoaDataInfo: AoaDataInfo) {
        // Save AoaDataInfo to Redis.
        redisTemplate.opsForValue().set("aoaData:${aoaDataInfo.deviceId}", aoaDataInfo)

        // Publish a message about the new AoaDataInfo.
        val message = UpdateMessage("AoaDataInfo", objectMapper.valueToTree(aoaDataInfo))
        redisTemplate.convertAndSend("updates", message)
    }

    fun listenForUpdates() {
        val connection = redisTemplate.connectionFactory?.connection
        connection?.subscribe(RedisMessageListener(), "updates".toByteArray())
    }

    private inner class RedisMessageListener : MessageListener {
        override fun onMessage(message: Message, pattern: ByteArray?) {
            // Deserialize the UpdateMessage from the message.
            val updateMessage = deserialize(message.body) as UpdateMessage

            when (updateMessage.type) {
                "BeaconInfo" -> updateBeaconInfo(objectMapper.treeToValue(updateMessage.data, BeaconInfo::class.java))
                "AoaDataInfo" -> insertAoaDataInfo(objectMapper.treeToValue(updateMessage.data, AoaDataInfo::class.java))
                // handle other types as needed...
            }
        }
    }

    private fun getGatewayInfoFromRedis(gatewayId: String): GatewayInfo {
        val gatewayInfo = redisTemplate.opsForValue().get("gateway:$gatewayId")
        if (gatewayInfo != null) {
            return gatewayInfo as GatewayInfo
        } else {
            // If GatewayInfo is not in Redis, get it from database and save it in Redis.
            val gatewayInfoFromDb = getGatewayInfoFromDatabase(gatewayId)
            redisTemplate.opsForValue().set("gateway:$gatewayId", gatewayInfoFromDb)
            return gatewayInfoFromDb
        }
    }

    private fun getBeaconInfoFromRedis(deviceId: String): BeaconInfo? {
        val beaconInfo = redisTemplate.opsForValue().get("beacon:$deviceId")
        if (beaconInfo != null) {
            return beaconInfo as BeaconInfo
        } else {
            // If BeaconInfo is not in Redis, get it from database and save it in Redis.
            val beaconInfoFromDb = getBeaconInfoFromDatabase(deviceId)
            redisTemplate.opsForValue().set("beacon:$deviceId", beaconInfoFromDb)
            return beaconInfoFromDb
        }
    }

    private fun getGatewayInfoFromDatabase(gatewayId: String): GatewayInfo {
        // Get GatewayInfo from database.
        // Implementation depends on your database and ORM.
        return GatewayInfo().selectById(gatewayId)
    }

    private fun getBeaconInfoFromDatabase(deviceId: String): BeaconInfo {
        // Get BeaconInfo from database.
        // Implementation depends on your database and ORM.
        return BeaconInfo().selectById(deviceId)
    }

    fun insertAoaDataInfo(aoaDataInfo: AoaDataInfo) {
        // Insert AoaDataInfo into database.
        aoaDataInfo.insert()
    }

    fun updateBeaconInfo(beaconInfo: BeaconInfo) {
        // Update BeaconInfo in database.
        beaconInfo.updateById()
    }
}



