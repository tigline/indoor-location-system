package com.lgguan.iot.position.service

import com.lgguan.iot.position.entity.AoaDataInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import com.lgguan.iot.position.entity.BeaconInfo
import com.lgguan.iot.position.entity.GatewayInfo
import com.lgguan.iot.position.external.model.AoaData
import com.fasterxml.jackson.databind.ObjectMapper
import com.lgguan.iot.position.bean.MessageType
import com.lgguan.iot.position.bean.WsMessage
import com.lgguan.iot.position.external.ExternalFenceHandler
import com.lgguan.iot.position.external.model.toInfo
import com.lgguan.iot.position.util.Point
import com.lgguan.iot.position.ws.sendWsMessage
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.hibernate.query.sqm.tree.SqmNode.log


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
        aoaDataTask.setGatewayAndBeaconService(this)
    }

    fun handleAoaData(aoaData: AoaData, externalFenceHandler: ExternalFenceHandler) {
//        val gatewayId = aoaData.gateway
//        val deviceId = aoaData.nodeId

        val gatewayInfo: GatewayInfo = getGatewayInfoFromRedis(aoaData.gateway)
        val aoaDataInfo = aoaData.toInfo()
        val beaconInfo: BeaconInfo = getBeaconInfoFromRedis(aoaDataInfo.deviceId.toString())

        if (beaconInfo != null) {
//            val prevPoint = Point(beaconInfo.posX ?: 0f, beaconInfo.posY ?: 0f)

            aoaDataInfo.mapId = gatewayInfo?.mapId

            aoaDataInfo.type = beaconInfo?.type

            CoroutineScope(Dispatchers.IO).launch {
                log.info("Received AoaDataInfo message: ${aoaDataInfo}")
                sendWsMessage(WsMessage(MessageType.AOAData, aoaDataInfo))
//                if ("freezing" != beaconInfo.motion) {
//                    externalFenceHandler.emit(beaconInfo to prevPoint)
//                }
            }


            aoaDataTask.addTaskToQueue("AoaDataInfo", aoaDataInfo)
            beaconInfo.apply {
                mac = aoaData.mac
                gateway = aoaData.gateway
                mapId = gatewayInfo?.mapId ?: mapId
                systemId = aoaData.systemId
                motion = aoaData.motion
                optScale = aoaData.optScale
                positionType = aoaData.positionType
                posX = aoaData.posX
                posY = aoaData.posY
                online = true
                updateTime = aoaDataInfo.timestamp
            }
            aoaDataTask.addTaskToQueue("BeaconInfo", beaconInfo)

        }

        // Now you have GatewayInfo and BeaconInfo, you can process aoaData...
    }
    //redisTemplate.opsForValue().set("beacon:$deviceId", beaconInfo)
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

    private fun getBeaconInfoFromRedis(deviceId: String): BeaconInfo {
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
