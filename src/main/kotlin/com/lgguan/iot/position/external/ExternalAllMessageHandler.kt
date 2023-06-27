package com.lgguan.iot.position.external

import cn.hutool.core.date.DateUtil
import cn.hutool.json.JSONUtil
import com.fasterxml.jackson.core.type.TypeReference
import com.lgguan.iot.position.bean.*
import com.lgguan.iot.position.entity.AoaDataInfo
import com.lgguan.iot.position.entity.BeaconInfo
import com.lgguan.iot.position.entity.GatewayInfo
import com.lgguan.iot.position.service.IAoaDataInfoService
import com.lgguan.iot.position.service.IBeaconInfoService
import com.lgguan.iot.position.service.IGatewayInfoService
import com.lgguan.iot.position.util.Point
import com.lgguan.iot.position.util.objectMapper
import com.lgguan.iot.position.ws.sendWsMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.commons.collections.CollectionUtils
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.util.*
import java.util.function.Function
import java.util.stream.Collectors
import kotlin.collections.ArrayList

@Component
class ExternalAllMessageHandler(val externalFenceHandler: ExternalFenceHandler,
                                val beaconInfoService: IBeaconInfoService,
                                val aoaDataInfoService: IAoaDataInfoService,
                                val gatewayInfoService: IGatewayInfoService) {
    private val log = LoggerFactory.getLogger(javaClass)

    val TYPE_LOCATORS = "locators" //基站
    val TYPE_SENSORS = "sensors" //信标

    fun handler(payload: String, topic: String, companyCode: String, type: String){
        when (type) {

            TYPE_LOCATORS -> {
                val stationDataList: List<IotStationData> = objectMapper.readValue(payload, object : TypeReference<List<IotStationData>>() {})
                if(CollectionUtils.isEmpty(stationDataList)){
                    log.warn("station gateway data list is empty!")
                    return
                }
                log.info("station gateway data: "+JSONUtil.toJsonStr(stationDataList))
                val stationDataMap = stationDataList.stream()
                    .collect(Collectors.toMap(IotStationData::gateway, Function.identity()))
                val gatewayInfos: List<GatewayInfo> = gatewayInfoService.listByIds(stationDataMap.keys)
                if(CollectionUtils.isNotEmpty(gatewayInfos)){
                    gatewayInfos.forEach { gatewayInfo ->
                        val stationData = stationDataMap[gatewayInfo.gateway]
                        if(stationData != null){
                            gatewayInfo.ip = stationData.ip
                            gatewayInfo.status = if(stationData.online == 1) OnlineStatus.Online else OnlineStatus.Offline
                            gatewayInfo.updateTime = stationData.time
                            gatewayInfo.extraInfo = stationData.angles
                            gatewayInfo.name = stationData.name
//                            gatewayInfo.type = stationData.type
                            gatewayInfo.mapId = stationData.mapId
                            gatewayInfo.zoneId = stationData.zoneId
                            gatewayInfo.hisX = stationData.hisX
                            gatewayInfo.hisY = stationData.hisY
                            gatewayInfo.hisZ = stationData.hisZ
                        }
                    }
                    gatewayInfoService.batchUpdate(gatewayInfos)
                }
            }

            TYPE_SENSORS -> {
                val beaconDataList: List<IotBeaconData> = objectMapper.readValue(payload, object : TypeReference<List<IotBeaconData>>() {})
                if(CollectionUtils.isEmpty(beaconDataList)){
                    log.warn("beacon data list is empty!")
                    return
                }
                log.info("beacon data: "+JSONUtil.toJsonStr(beaconDataList))

                val aoaDataInfoList = ArrayList<AoaDataInfo>()
                val beaconInfoUpdates = ArrayList<BeaconInfo>()
                val iotBeaconDataMap = beaconDataList.stream()
                    .collect(Collectors.toMap(IotBeaconData::beaconId, Function.identity()))
                val beaconInfos: List<BeaconInfo> = beaconInfoService.listByIds(iotBeaconDataMap.keys)
                beaconInfos.forEach {
                    val aoaDataInfo = iotBeaconDataMap[it.deviceId]?.let { it1 -> getAoaDataInfo(it1) }
                    if (aoaDataInfo != null) {
                        it.gateway = aoaDataInfo?.gatewayId
                        it.mapId = aoaDataInfo?.mapId
                        it.zoneId = aoaDataInfo?.zoneId
                        it.optScale = aoaDataInfo?.optScale
                        it.posX = aoaDataInfo?.posX
                        it.posY = aoaDataInfo?.posY
                        it.posZ = aoaDataInfo?.posZ
                        it.updateTime = aoaDataInfo?.timestamp
                        it.online = true
                        beaconInfoUpdates.add(it)
                        aoaDataInfoList.add(aoaDataInfo)
                    }
                }

                if(CollectionUtils.isNotEmpty(beaconInfoUpdates)
                    && CollectionUtils.isNotEmpty(aoaDataInfoList)){
                    beaconInfoService.batchUpdate(beaconInfoUpdates)
                    aoaDataInfoService.batchInsert(aoaDataInfoList)
                    asyncSendMessage(beaconInfoUpdates, aoaDataInfoList)
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
        aoaDataInfo.createTime = Date()
        return aoaDataInfo
    }

    @Async("wsTaskExecutor")
    fun asyncSendMessage(beaconInfoUpdates: List<BeaconInfo>, aoaDataInfoList: List<AoaDataInfo>) {
        val aoaDataInfoMap = aoaDataInfoList.stream()
            .collect(Collectors.toMap(AoaDataInfo::deviceId, Function.identity()))
        beaconInfoUpdates.forEach {beaconInfo ->
            if(aoaDataInfoMap.containsKey(beaconInfo.deviceId)){
                CoroutineScope(Dispatchers.IO).launch {
                    sendWsMessage(WsMessage(MessageType.AOAData, aoaDataInfoMap[beaconInfo.deviceId]))
                    if ("freezing" != beaconInfo.motion) {
                        val prevPoint = Point(beaconInfo.posX ?: 0f, beaconInfo.posY ?: 0f)
                        externalFenceHandler.emit(beaconInfo to prevPoint)
                    }
                }
            }
        }
    }
}