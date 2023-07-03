package com.lgguan.iot.position.external

import com.lgguan.iot.position.bean.MessageType
import com.lgguan.iot.position.bean.OnlineStatus
import com.lgguan.iot.position.bean.WsMessage
import com.lgguan.iot.position.entity.BeaconInfo
import com.lgguan.iot.position.entity.GatewayInfo
import com.lgguan.iot.position.external.model.AoaData
import com.lgguan.iot.position.external.model.AoaStationHis
import com.lgguan.iot.position.external.model.StationOnlineStatus
import com.lgguan.iot.position.external.model.toInfo
import com.lgguan.iot.position.util.Point
import com.lgguan.iot.position.util.objectMapper
import com.lgguan.iot.position.ws.sendWsMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 *
 *
 * @author N.Liu
 **/
@Component
class ExternalMqttMessageHandler(val externalFenceHandler: ExternalFenceHandler) {
    private val log = LoggerFactory.getLogger(javaClass)
    fun handler(topic: String, payload: String) {
        when (topic) {

            "/ips/pub/AOA_data" -> {
                val aoaData = objectMapper.readValue(payload, AoaData::class.java)
                val aoaDataInfo = aoaData.toInfo()
                val beaconInfo = BeaconInfo().selectById(aoaDataInfo.deviceId)
                if (beaconInfo != null) {
                    val prevPoint = Point((beaconInfo.posX ?: 0) as Double, (beaconInfo.posY ?: 0) as Double)
                    val gatewayInfo: GatewayInfo? = GatewayInfo().selectById(aoaData.gateway)
                    beaconInfo.apply {
                        mac = aoaData.mac
                        gateway = aoaData.gateway
                        mapId = gatewayInfo?.mapId ?: mapId
                        systemId = aoaData.systemId
                        zoneId = aoaData.zoneId
                        motion = aoaData.motion
                        optScale = aoaData.optScale
                        positionType = aoaData.positionType
                        posX = aoaData.posX
                        posY = aoaData.posY
                        online = true
                        updateTime = aoaDataInfo.timestamp
                    }.updateById()
                    aoaDataInfo.type = beaconInfo.type
                    aoaDataInfo.mapId = beaconInfo.mapId
                    aoaDataInfo.zoneId = beaconInfo.zoneId
                    aoaDataInfo.status = if (beaconInfo.motion == "freezing") 0 else 1
                    aoaDataInfo.insert()
                    CoroutineScope(Dispatchers.IO).launch {
                        sendWsMessage(WsMessage(MessageType.AOAData, aoaDataInfo))
                        if ("freezing" != beaconInfo.motion) {
                            externalFenceHandler.emit(beaconInfo)
                        }
                    }
                }
            }

            "/ips/pub/station_online_status" -> {
                val stationOnlineStatus = objectMapper.readValue(payload, StationOnlineStatus::class.java)
                log.info("Received [$topic] message: $stationOnlineStatus")
                stationOnlineStatus.onlineStatus.forEach { onlineStatus ->
                    GatewayInfo().selectById(onlineStatus.gateway)?.apply {
                        systemId = stationOnlineStatus.systemId
                        status = onlineStatus.status
                        ip = onlineStatus.ip
                    }?.updateById()
                }
            }

            "/ips/pub/AOA_Station_His" -> {
                val aoaStationHis = objectMapper.readValue(payload, AoaStationHis::class.java)
                log.info("Received [$topic] message: $aoaStationHis")
                val gatewayInfo = GatewayInfo().selectById(aoaStationHis.gateway)
                gatewayInfo?.apply {
                    systemId = aoaStationHis.systemId
                    status = OnlineStatus.Online
                    hisX = aoaStationHis.hisX
                    hisY = aoaStationHis.hisY
                    hisZ = aoaStationHis.hisZ
                }?.updateById()
            }

            else -> {
                log.warn("Unsupported topic [$topic]")
            }
        }
    }
}
