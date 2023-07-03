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
import com.lgguan.iot.position.util.objectMapper
import com.lgguan.iot.position.ws.sendWsMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import org.apache.commons.collections.CollectionUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*
import java.util.function.Function
import java.util.stream.Collectors

@Component
class ExternalAllMessageHandler(val externalFenceHandler: ExternalFenceHandler,
                                val beaconInfoService: IBeaconInfoService,
                                val aoaDataInfoService: IAoaDataInfoService,
                                val gatewayInfoService: IGatewayInfoService) {
    private val log = LoggerFactory.getLogger(javaClass)

    val TYPE_LOCATORS = "locators" //基站
    val TYPE_SENSORS = "sensors" //信标
    private val dbDispatcher = newSingleThreadContext("DBDispatcher")
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
                    .collect(Collectors.toMap(IotStationData::realGateway, Function.identity()))
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
                            //gatewayInfo.mapId = stationData.mapId
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
                var gatewayInfoMaps: Map<String, GatewayInfo> = getGatewayInfoByIds(beaconDataList)

                val iotBeaconDataMap = beaconDataList.stream()
                    .collect(Collectors.toMap(IotBeaconData::beaconId, Function.identity()))
                //beaconInfos 为存在的数据库
                val beaconInfos: List<BeaconInfo> = beaconInfoService.listByIds(iotBeaconDataMap.keys)

                beaconInfos.forEach {
                    val aoaDataInfo = iotBeaconDataMap[it.deviceId]?.let { it1 -> getAoaDataInfo(it1) }
                    if (aoaDataInfo != null) {
                        val xy = it.posX.toString() + "," + it.posY.toString()
                        it.gateway = aoaDataInfo?.gatewayId
                        it.zoneId = aoaDataInfo?.zoneId
                        it.optScale = aoaDataInfo?.optScale
                        it.extraInfo = xy
                        it.posX = aoaDataInfo?.posX
                        it.posY = aoaDataInfo?.posY
                        it.posZ = aoaDataInfo?.posZ
                        it.updateTime = aoaDataInfo?.timestamp
                        it.online = true
                        it.motion = if (aoaDataInfo.status == 1) "moving" else "freezing"
                        val gatewayInfo: GatewayInfo? = gatewayInfoMaps[aoaDataInfo.gatewayId]
                        it.mapId = gatewayInfo?.mapId
                        aoaDataInfo.mapId = gatewayInfo?.mapId
                        aoaDataInfo.type = it.type
                        beaconInfoUpdates.add(it)
                        aoaDataInfoList.add(aoaDataInfo)
                    }
                }

                if(CollectionUtils.isNotEmpty(beaconInfoUpdates)
                    && CollectionUtils.isNotEmpty(aoaDataInfoList)){
                    asyncSendMessage(beaconInfoUpdates, aoaDataInfoList)
                    CoroutineScope(dbDispatcher).launch {
                        beaconInfoService.batchUpdate(beaconInfoUpdates)
                        aoaDataInfoService.batchInsert(aoaDataInfoList)
                    }
                }
            }

            else -> {
                log.warn("Unsupported topic: [$topic], companyCode: [$companyCode], type: [$type]")
            }
        }
    }

    private fun getGatewayInfoByIds(beaconDataList: List<IotBeaconData>): Map<String, GatewayInfo> {
        if (CollectionUtils.isNotEmpty(beaconDataList)){
            val gatewayIds: List<String> = beaconDataList.stream().map<String>(IotBeaconData::gateway).distinct().collect(Collectors.toList())
            val gatewayInfos: List<GatewayInfo> = gatewayInfoService.listByIds(gatewayIds);
            if(CollectionUtils.isNotEmpty(gatewayInfos)){
                return gatewayInfos.stream().collect(Collectors.toMap(GatewayInfo::gateway, Function.identity()))
            }
        }
        return HashMap()
    }

    private fun getBeaconInfoByIds(beaconDataList: List<IotBeaconData>): Map<String, BeaconInfo> {
        if (CollectionUtils.isNotEmpty(beaconDataList)){
            val beaconIds: List<String> = beaconDataList.stream().map<String>(IotBeaconData::beaconId).distinct().collect(Collectors.toList())
            val beaconInfos: List<BeaconInfo> = beaconInfoService.listByIds(beaconIds);
            if(CollectionUtils.isNotEmpty(beaconInfos)){
                return beaconInfos.stream().collect(Collectors.toMap(BeaconInfo::deviceId, Function.identity()))
            }
        }
        return HashMap()
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

    fun asyncSendMessage(beaconInfoUpdates: List<BeaconInfo>, aoaDataInfoList: List<AoaDataInfo>) {
        val aoaDataInfoMap = aoaDataInfoList.stream()
            .collect(Collectors.toMap(AoaDataInfo::deviceId, Function.identity()))
        beaconInfoUpdates.forEach {beaconInfo ->
            if(aoaDataInfoMap.containsKey(beaconInfo.deviceId)){
                CoroutineScope(Dispatchers.IO).launch {
                    sendWsMessage(WsMessage(MessageType.AOAData, aoaDataInfoMap[beaconInfo.deviceId]))
                    log.info("beaconInfo-------posX:"+beaconInfo.posX + ", posY:"+beaconInfo.posY+", extraInfo:"+beaconInfo.extraInfo)
                    if ("freezing" != beaconInfo.motion) {
                        externalFenceHandler.emit(beaconInfo)
                    }
                }
            }
        }
    }
}