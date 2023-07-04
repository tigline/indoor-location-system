package com.lgguan.iot.position.external

import cn.hutool.core.date.DateUtil
import cn.hutool.json.JSONUtil
import com.lgguan.iot.position.bean.*
import com.lgguan.iot.position.entity.AoaDataInfo
import com.lgguan.iot.position.entity.BeaconInfo
import com.lgguan.iot.position.entity.GatewayInfo
import com.lgguan.iot.position.util.objectMapper
import com.lgguan.iot.position.ws.sendWsMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

@Component
class ExternalAllMessageHandler(val externalFenceHandler: ExternalFenceHandler) {
    private val log = LoggerFactory.getLogger(javaClass)

    val TYPE_LOCATORS = "locators" //基站
    val TYPE_SENSORS = "sensors" //信标

    fun handler(payload: String, topic: String, companyCode: String, type: String){
        when (type) {

            TYPE_LOCATORS -> {
                val stationData: IotStationData = objectMapper.readValue(payload, IotStationData::class.java)
                if(Objects.isNull(stationData)){
                    log.warn("station gateway data list is empty!")
                    return
                }
                log.info("station gateway data: "+JSONUtil.toJsonStr(stationData))
                val gatewayInfo = GatewayInfo().selectById(stationData.gateway)
                gatewayInfo?.apply {
                    ip = stationData.ip
                    status = if(stationData.online == 1) OnlineStatus.Online else OnlineStatus.Offline
                    updateTime = stationData.time
                    extraInfo = stationData.angles
                    name = stationData.name
//                    type = stationData.type
//                    gatewayInfo.mapId = stationData.mapId
                    zoneId = stationData.zoneId
                    hisX = stationData.hisX
                    hisY = stationData.hisY
                    hisZ = stationData.hisZ
                }?.updateById()
            }

            TYPE_SENSORS -> {
                val beaconData: IotBeaconData = objectMapper.readValue(payload, IotBeaconData::class.java)
                if(Objects.isNull(beaconData)){
                    log.warn("beacon data list is empty!")
                    return
                }
                log.info("beacon data: "+JSONUtil.toJsonStr(beaconData))
                val beaconInfo = BeaconInfo().selectById(beaconData.beaconId)
                if(Objects.nonNull(beaconInfo)){
                    val aoaDataInfo = getAoaDataInfo(beaconData)
                    val gatewayInfo: GatewayInfo? = GatewayInfo().selectById(beaconData.gateway)
                    beaconInfo.apply {
                        gateway = aoaDataInfo?.gatewayId
                        zoneId = aoaDataInfo?.zoneId
                        optScale = aoaDataInfo?.optScale
                        extraInfo = posX.toString() + "," + posY.toString()
                        posX = aoaDataInfo?.posX
                        posY = aoaDataInfo?.posY
                        posZ = aoaDataInfo?.posZ
                        updateTime = aoaDataInfo?.timestamp
                        online = true
                        motion = if (aoaDataInfo.status == 1) "moving" else "freezing"
                        mapId = gatewayInfo?.mapId
                    }.updateById()
                    aoaDataInfo.mapId = gatewayInfo?.mapId
                    aoaDataInfo.type = beaconInfo.type
                    aoaDataInfo.insert()
                    CoroutineScope(Dispatchers.IO).launch {
                        sendWsMessage(WsMessage(MessageType.AOAData, aoaDataInfo))
                        log.info("beaconInfo-------posX:"+beaconInfo.posX + ", posY:"+beaconInfo.posY+", extraInfo:"+beaconInfo.extraInfo)
                        if ("freezing" != beaconInfo.motion) {
                            externalFenceHandler.emit(beaconInfo)
                        }
                    }
                }
            }

            else -> {
                log.warn("Unsupported topic: [$topic], companyCode: [$companyCode], type: [$type]")
            }
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
}