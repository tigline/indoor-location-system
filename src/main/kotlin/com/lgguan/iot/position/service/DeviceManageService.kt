package com.lgguan.iot.position.service

import cn.hutool.core.date.DateUtil
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.lgguan.iot.position.bean.*
import com.lgguan.iot.position.entity.AoaDataInfo
import com.lgguan.iot.position.entity.BeaconInfo
import com.lgguan.iot.position.entity.GatewayInfo
import com.lgguan.iot.position.ext.convert
import com.lgguan.iot.position.external.AcServerApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 *
 *
 * @author N.Liu
 **/
@Service
@Transactional(rollbackFor = [Exception::class])
class DeviceManageService(
    val gatewayInfoService: IGatewayInfoService,
    val beaconInfoService: IBeaconInfoService,
    val aoaDataInfoService: IAoaDataInfoService,
    val personnelInfoService: IPersonnelInfoService,
    val thingInfoService: IThingInfoService,
    val acServerApi: AcServerApi
) {
    fun pageGatewayInfos(param: QueryGatewayParam, pageLimit: PageLimit): PageResult<GatewayInfo> {
        return gatewayInfoService.page(
            pageLimit.convert(), KtQueryWrapper(GatewayInfo::class.java)
                .eq(!param.mapId.isNullOrEmpty(), GatewayInfo::mapId, param.mapId)
                .like(!param.gateway.isNullOrEmpty(), GatewayInfo::gateway, param.gateway)
                .like(!param.name.isNullOrEmpty(), GatewayInfo::name, param.name)
        ).convert()
    }

    fun addGatewayInfo(addGatewayInfo: AddGatewayInfo): RestValue<Boolean> {
        if (gatewayInfoService.getById(addGatewayInfo.gateway) != null) {
            return failedOf(IErrorCode.DataExists, "Gateway [${addGatewayInfo.gateway}] already exists")
        }
        val gatewayInfo = GatewayInfo().apply {
            gateway = addGatewayInfo.gateway
            mapId = addGatewayInfo.mapId
            groupId = addGatewayInfo.groupId
            name = addGatewayInfo.name
            productName = addGatewayInfo.productName
            status = OnlineStatus.Offline
            type = addGatewayInfo.type
            setX = addGatewayInfo.setX
            setY = addGatewayInfo.setY
            setZ = addGatewayInfo.setZ
            angle = addGatewayInfo.angle
            updateTime = DateUtil.currentSeconds()
        }
        val save = gatewayInfoService.save(gatewayInfo)
        if (save) {
            CoroutineScope(Dispatchers.IO).launch {
                acServerApi.updateGatewayLocation(
                    gatewayInfo.gateway!!,
                    gatewayInfo.setX,
                    gatewayInfo.setY,
                    gatewayInfo.setZ,
                    gatewayInfo.angle,
                    gatewayInfo.mapId
                )
            }
        }
        return okOf(save)
    }

    fun updateGatewayInfo(gateway: String, updateGateway: UpdateGateway): RestValue<Boolean> {
        val gatewayInfo = gatewayInfoService.getById(gateway)
        gatewayInfo ?: return failedOf(IErrorCode.DataNotExists, "Gateway [$gateway] not exists")
        gatewayInfo.apply {
            name = updateGateway.name
            mapId = updateGateway.mapId
            setX = updateGateway.setX
            setY = updateGateway.setY
            setZ = updateGateway.setZ
            angle = updateGateway.angle
            updateTime = DateUtil.currentSeconds()
        }
        val update = gatewayInfoService.updateById(gatewayInfo)
        if (update) {
            CoroutineScope(Dispatchers.IO).launch {
                acServerApi.updateGatewayLocation(
                    gatewayInfo.gateway!!,
                    gatewayInfo.setX,
                    gatewayInfo.setY,
                    gatewayInfo.setZ,
                    gatewayInfo.angle,
                    gatewayInfo.mapId
                )
            }
        }
        return okOf(update)
    }

    fun deleteGatewayInfo(gateway: String): RestValue<Boolean> {
        val gatewayInfo = gatewayInfoService.getById(gateway)
        gatewayInfo ?: return failedOf(IErrorCode.DataNotExists, "Gateway [$gateway] not exists")
        val res = gatewayInfoService.removeById(gateway)
        return okOf(res)
    }

    fun getGatewayOnlineStatus(mapId: String?): OnlineStatusCount {
        val gatewayMap = gatewayInfoService.list(
            KtQueryWrapper(GatewayInfo::class.java)
                .eq(!mapId.isNullOrEmpty(), GatewayInfo::mapId, mapId)
        ).groupBy { it.status }
        val online = gatewayMap[OnlineStatus.Online]?.size ?: 0
        val offline = gatewayMap[OnlineStatus.Offline]?.size ?: 0
        val total = online + offline
        return OnlineStatusCount(total, online, offline)
    }

    fun pageBeaconInfos(param: QueryBeaconParam, pageLimit: PageLimit): PageResult<BeaconInfo> {
        return beaconInfoService.page(
            pageLimit.convert(), KtQueryWrapper(BeaconInfo::class.java)
                .like(!param.deviceId.isNullOrEmpty(), BeaconInfo::deviceId, param.deviceId)
                .like(!param.name.isNullOrEmpty(), BeaconInfo::name, param.name)
        ).convert()
    }

    fun addBeaconInfo(addBeaconInfo: AddBeaconInfo): RestValue<Boolean> {
        if (beaconInfoService.getById(addBeaconInfo.deviceId) != null) {
            return failedOf(IErrorCode.DataExists, "DeviceId [${addBeaconInfo.deviceId}] already exists")
        }
        val beaconInfo = BeaconInfo().apply {
            deviceId = addBeaconInfo.deviceId
            mac = addBeaconInfo.mac
            gateway = addBeaconInfo.gateway
            mapId = addBeaconInfo.mapId
            groupId = addBeaconInfo.groupId
            name = addBeaconInfo.name
            productName = addBeaconInfo.productName
            status = BindStatus.Unbound
            type = addBeaconInfo.type
        }
        val save = beaconInfoService.save(beaconInfo)
        return okOf(save)
    }

    fun updateBeaconInfo(deviceId: String, updateBeacon: UpdateBeacon): RestValue<Boolean> {
        val beaconInfo = beaconInfoService.getById(deviceId)
        beaconInfo ?: return failedOf(IErrorCode.DataNotExists, "Beacon [$deviceId] not exists")
        beaconInfo.apply {
            name = updateBeacon.name
            type = updateBeacon.type
            fenceIds = updateBeacon.fenceIds
        }
        val res = beaconInfoService.updateById(beaconInfo)
        return okOf(res)
    }

    fun deleteBeaconInfo(deviceId: String): RestValue<Boolean> {
        val beaconInfo = beaconInfoService.getById(deviceId)
        beaconInfo ?: return failedOf(IErrorCode.DataNotExists, "Beacon [$deviceId] not exists")
        if (beaconInfo.status == BindStatus.Bound) {
            personnelInfoService.removeTag(deviceId)
            thingInfoService.removeTag(deviceId)
        }
        aoaDataInfoService.remove(KtQueryWrapper(AoaDataInfo::class.java).eq(AoaDataInfo::deviceId, deviceId))
        val res = beaconInfoService.removeById(deviceId)
        return okOf(res)
    }

    fun listUnboundBeaconInfo(type: BeaconType?): List<BeaconInfo> {
        return beaconInfoService.list(
            KtQueryWrapper(BeaconInfo::class.java)
                .eq(BeaconInfo::status, BindStatus.Unbound)
                .eq(type != null, BeaconInfo::type, type)
        )
    }

    fun listBeaconLocation(param: QueryBeaconLocationParam): List<AoaDataInfo> {
        return aoaDataInfoService.list(
            KtQueryWrapper(AoaDataInfo::class.java)
                .eq(AoaDataInfo::mapId, param.mapId)
                .eq(!param.deviceId.isNullOrEmpty(), AoaDataInfo::deviceId, param.deviceId)
                .between(AoaDataInfo::timestamp, param.startTime, param.endTime)
                .orderByAsc(AoaDataInfo::timestamp)
        )
    }

    fun listBeaconLocationWithMovingAverage(param: QueryBeaconLocationParam): List<AoaDataInfo> {
        val rawList = aoaDataInfoService.list(
            KtQueryWrapper(AoaDataInfo::class.java) //.eq(AoaDataInfo::status,1)
                .eq(AoaDataInfo::mapId, param.mapId)
                .eq(!param.deviceId.isNullOrEmpty(), AoaDataInfo::deviceId, param.deviceId)
                .between(AoaDataInfo::timestamp, param.startTime, param.endTime)
                .orderByAsc(AoaDataInfo::timestamp)
        )

        //
        val sampledList = mutableListOf<AoaDataInfo>()

        for (i in rawList.indices step 2) {
            sampledList.add(rawList[i])
        }

        // 去除status为0的数据
        val preprocessedList = mutableListOf<AoaDataInfo>()
        var lastIndex = -1
        for (i in sampledList.indices) {
            if (sampledList[i].status == 0) {
                lastIndex = i
            } else {
                if (lastIndex != -1) {
                    preprocessedList.add(sampledList[lastIndex])
                    lastIndex = -1
                }
                preprocessedList.add(sampledList[i])
            }
        }


        // 滑动窗口滤波
        val windowSize = param.filterValue ?: 0
        val filteredList = mutableListOf<AoaDataInfo>()
        for (i in windowSize until preprocessedList.size - windowSize) {
            val subList = preprocessedList.subList(i - windowSize, i + windowSize + 1)
            val avgPosX = subList.map { it.posX ?: 0f }.sum() / subList.size
            val avgPosY = subList.map { it.posY ?: 0f }.sum() / subList.size
            val filteredInfo = AoaDataInfo()
            filteredInfo.copyFrom(preprocessedList[i])
            filteredInfo.deviceId = param.deviceId
            filteredInfo.mapId = param.mapId
            filteredInfo.posX = avgPosX
            filteredInfo.posY = avgPosY
            filteredInfo.timestamp = subList.last().timestamp
            filteredList.add(filteredInfo)
        }
        return filteredList
    }

    fun getBeaconOnlineStatusCounts(
        mapId: String?,
        type: BeaconType?,
        onlyOnline: Boolean?
    ): MutableMap<BeaconType, OnlineStatusCount> {
        val typesMap = beaconInfoService.list(
            KtQueryWrapper(BeaconInfo::class.java)
                .eq(!mapId.isNullOrEmpty(), BeaconInfo::mapId, mapId)
                .eq(type != null, BeaconInfo::type, type)
                .eq(onlyOnline ?: false, BeaconInfo::online, true)
        ).groupBy { it.type }
        val statusMap = mutableMapOf<BeaconType, OnlineStatusCount>()
        BeaconType.values().filter { if (type != null) it == type else true }
            .forEach {
                val beaconMap = typesMap[it]?.groupBy { beacon -> beacon.online }
                val online = beaconMap?.get(true)?.size ?: 0
                val offline = beaconMap?.get(false)?.size ?: 0
                val total = online + offline
                statusMap[it] = OnlineStatusCount(total, online, offline)
            }
        return statusMap
    }
}
