package com.lgguan.iot.position.external

import cn.hutool.core.date.DateUtil
import cn.hutool.json.JSONUtil
import com.fasterxml.jackson.core.type.TypeReference
import com.lgguan.iot.position.bean.IotBeaconData
import com.lgguan.iot.position.bean.IotStationData
import com.lgguan.iot.position.bean.MessageType
import com.lgguan.iot.position.bean.WsMessage
import com.lgguan.iot.position.entity.AoaDataInfo
import com.lgguan.iot.position.entity.BeaconInfo
import com.lgguan.iot.position.service.IAoaDataInfoService
import com.lgguan.iot.position.service.IBeaconInfoService
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
                                val aoaDataInfoService: IAoaDataInfoService) {
    private val log = LoggerFactory.getLogger(javaClass)

    val TYPE_LOCATORS = "locators" //基站
    val TYPE_SENSORS = "sensors" //信标

    fun handler(payload: String, topic: String, companyCode: String, type: String){
        when (type) {

            TYPE_LOCATORS -> {
                val stationData: List<IotStationData> = objectMapper.readValue(payload, object : TypeReference<List<IotStationData>>() {})
                log.info("station data: "+JSONUtil.toJsonStr(stationData))


            }

            TYPE_SENSORS -> {
                val beaconDataList: List<IotBeaconData> = objectMapper.readValue(payload, object : TypeReference<List<IotBeaconData>>() {})
                if(CollectionUtils.isEmpty(beaconDataList)){
                    log.warn("beacon data list is empty!")
                    return
                }
                log.info("beacon data: "+JSONUtil.toJsonStr(beaconDataList))

                val aoaDataInfoList = ArrayList<AoaDataInfo>()
                beaconDataList.forEach { beaconData ->
                    val aoaDataInfo = AoaDataInfo()
                    aoaDataInfo.deviceId = beaconData.beaconId
                    aoaDataInfo.gatewayId = beaconData.gateway
                    aoaDataInfo.posX = beaconData.posX
                    aoaDataInfo.posY = beaconData.posY
                    aoaDataInfo.posZ = beaconData.posZ
                    aoaDataInfo.timestamp = if(Objects.nonNull(beaconData.time)) beaconData.time else DateUtil.currentSeconds()
                    aoaDataInfo.mapId = beaconData.mapId
                    aoaDataInfo.zoneId = beaconData.zoneId
                    aoaDataInfo.createTime = Date()
                    aoaDataInfoList.add(aoaDataInfo)
                }
                aoaDataInfoService.batchInsert(aoaDataInfoList)

                if(CollectionUtils.isNotEmpty(aoaDataInfoList)
                    && CollectionUtils.isNotEmpty(beaconDataList)){
                    val beaconInfoUpdates = ArrayList<BeaconInfo>()
                    val aoaDataInfoMap = aoaDataInfoList.stream()
                        .collect(Collectors.toMap(AoaDataInfo::deviceId, Function.identity()))
                    val beaconIds: List<String> = beaconDataList.stream()
                        .map<String>(IotBeaconData::beaconId).collect(Collectors.toList())
                    val beaconInfos: List<BeaconInfo> = beaconInfoService.listByIds(beaconIds)
                    beaconInfos.forEach {
                        if(aoaDataInfoMap.containsKey(it.deviceId)){
                            val aoaDataInfo = aoaDataInfoMap[it.deviceId]
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
                            if (aoaDataInfo != null) {
                                asyncSendMessage(it, aoaDataInfo)
                            }
                        }
                    }
                    beaconInfoService.batchUpdate(beaconInfoUpdates)
                }
            }

            else -> {
                log.warn("Unsupported topic: [$topic], companyCode: [$companyCode], type: [$type]")
            }
        }

    }

    @Async("wsTaskExecutor")
    fun asyncSendMessage(beaconInfo: BeaconInfo, aoaDataInfo: AoaDataInfo) {
        CoroutineScope(Dispatchers.IO).launch {
            sendWsMessage(WsMessage(MessageType.AOAData, aoaDataInfo))
            if ("freezing" != beaconInfo.motion) {
                val prevPoint = Point(beaconInfo.posX ?: 0f, beaconInfo.posY ?: 0f)
                externalFenceHandler.emit(beaconInfo to prevPoint)
            }
        }
    }
}