package com.lgguan.iot.position.service

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.lgguan.iot.position.bean.*
import com.lgguan.iot.position.entity.BuildingInfo
import com.lgguan.iot.position.entity.FenceInfo
import com.lgguan.iot.position.entity.MapInfo
import com.lgguan.iot.position.ext.convert
import com.lgguan.iot.position.util.fenceListCache
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(rollbackFor = [Exception::class])
class SystemManageService(
    val buildingInfoService: IBuildingInfoService,
    val mapInfoService: IMapInfoService,
    val fenceInfoService: IFenceInfoService
) {
    fun listBuildingInfos(name: String?): List<BuildingInfo> {
        return buildingInfoService.list(
            KtQueryWrapper(BuildingInfo::class.java)
                .like(!name.isNullOrEmpty(), BuildingInfo::name, name)
        )
    }

    fun addBuildingInfo(addBuilding: AddOrUpdateBuilding): Boolean {
        val buildingInfo = BuildingInfo().apply {
            name = addBuilding.name
            address = addBuilding.address
            picture = addBuilding.picture
        }
        return buildingInfoService.save(buildingInfo)
    }

    fun updateBuildingInfo(buildingId: String, updateBuilding: AddOrUpdateBuilding): RestValue<Boolean> {
        val buildingInfo = buildingInfoService.getById(buildingId)
        buildingInfo ?: return failedOf(IErrorCode.DataNotExists, "BuildingId [$buildingId] not exists")
        buildingInfo.apply {
            name = updateBuilding.name
            address = updateBuilding.address
            picture = updateBuilding.picture
        }
        val res = buildingInfoService.updateById(buildingInfo)
        return okOf(res)
    }

    fun deleteBuildingInfo(buildingId: String): RestValue<Boolean> {
        val buildingInfo = buildingInfoService.getById(buildingId)
        buildingInfo ?: return failedOf(IErrorCode.DataNotExists, "BuildingId [$buildingId] not exists")
        val mapCount = mapInfoService.count(KtQueryWrapper(MapInfo::class.java).eq(MapInfo::buildingId, buildingId))
        if (mapCount > 0) {
            return failedOf(IErrorCode.DataExists, "Building presence map data")
        }
        val res = buildingInfoService.removeById(buildingId)
        return okOf(res)
    }

    fun listMapInfos(param: QueryMapParam): List<MapInfo> {
        return mapInfoService.list(
            KtQueryWrapper(MapInfo::class.java)
                .select(MapInfo::class.java) {
                    if (param.hidePicture == true) {
                        it.property != "picture"
                    } else {
                        true
                    }
                }
                .eq(!param.buildingId.isNullOrEmpty(), MapInfo::buildingId, param.buildingId)
                .like(!param.name.isNullOrEmpty(), MapInfo::name, param.name)
        )
    }

    fun getMapInfo(mapId: String): RestValue<MapInfo> {
        val mapInfo = mapInfoService.getById(mapId)
        mapInfo ?: return failedOf(IErrorCode.DataNotExists, "MapId [$mapId] not exists")
        return okOf(mapInfo)
    }

    fun addMapInfo(addMapInfo: AddOrUpdateMapInfo): Boolean {
        val mapInfo = MapInfo().apply {
            name = addMapInfo.name
            buildingId = addMapInfo.buildingId
            floor = addMapInfo.floor
            picture = addMapInfo.picture
            width = addMapInfo.width
            length = addMapInfo.length
            widthPx = addMapInfo.widthPx
            lengthPx = addMapInfo.lengthPx
        }
        return mapInfoService.save(mapInfo)
    }

    fun updateMapInfo(mapId: String, updateMapInfo: AddOrUpdateMapInfo): RestValue<Boolean> {
        val mapInfo =
            mapInfoService.getById(mapId) ?: return failedOf(IErrorCode.DataNotExists, "MapId [$mapId] not exists")
        mapInfo.apply {
            name = updateMapInfo.name
            buildingId = updateMapInfo.buildingId
            floor = updateMapInfo.floor
            picture = updateMapInfo.picture
            width = updateMapInfo.width
            length = updateMapInfo.length
            widthPx = updateMapInfo.widthPx
            lengthPx = updateMapInfo.lengthPx
        }
        val update = mapInfoService.updateById(mapInfo)
        return okOf(update)
    }

    fun deleteMapInfo(mapId: String): RestValue<Boolean> {
        mapInfoService.getById(mapId) ?: return failedOf(IErrorCode.DataNotExists, "MapId [$mapId] not exists")
        val fenceCount = fenceInfoService.count(KtQueryWrapper(FenceInfo::class.java).eq(FenceInfo::mapId, mapId))
        if (fenceCount > 0) {
            return failedOf(IErrorCode.DataExists, "Map presence fence data")
        }
        val res = mapInfoService.removeById(mapId)
        return okOf(res)
    }

    fun pageFenceAndMap(param: QueryFenceAndMapParam, pageLimit: PageLimit): PageResult<FenceAndMapInfo> {
        return fenceInfoService.pageFenceAndMap(pageLimit.convert(), param).convert()
    }

    fun getFenceInfoById(fenceId: String): RestValue<FenceInfo> {
        val fenceInfo = fenceInfoService.getById(fenceId)
        fenceInfo ?: return failedOf(IErrorCode.DataNotExists, "FenceId [$fenceId] not exists")
        return okOf(fenceInfo)
    }

    fun addFenceInfo(addFenceInfo: AddOrUpdateFenceInfo): Boolean {
        val fenceInfo = FenceInfo().apply {
            name = addFenceInfo.name
            mapId = addFenceInfo.mapId
            type = addFenceInfo.type
            points = addFenceInfo.points
        }
        val save = fenceInfoService.save(fenceInfo)
        fenceListCache.invalidate(fenceInfo.mapId)
        return save
    }

    fun updateFenceInfo(fenceId: String, updateFenceInfo: AddOrUpdateFenceInfo): RestValue<Boolean> {
        val fenceInfo = fenceInfoService.getById(fenceId)
        fenceInfo ?: return failedOf(IErrorCode.DataNotExists, "FenceId [$fenceId] not exists")
        fenceInfo.apply {
            name = updateFenceInfo.name
            mapId = updateFenceInfo.mapId
            type = updateFenceInfo.type
            points = updateFenceInfo.points
        }
        val update = fenceInfoService.updateById(fenceInfo)
        fenceListCache.invalidate(fenceInfo.mapId)
        return okOf(update)
    }

    fun deleteFenceInfo(fenceId: String): RestValue<Boolean> {
        val fenceInfo = fenceInfoService.getById(fenceId)
        fenceInfo ?: return failedOf(IErrorCode.DataNotExists, "FenceId [$fenceId] not exists")
        val delete = fenceInfoService.removeById(fenceId)
        fenceListCache.invalidate(fenceInfo.mapId)
        return okOf(delete)
    }

    fun changeFenceStatus(fenceId: String): RestValue<Boolean> {
        val fenceInfo = fenceInfoService.getById(fenceId)
        fenceInfo ?: return failedOf(IErrorCode.DataNotExists, "FenceId [$fenceId] not exists")
        fenceInfo.enabled = !fenceInfo.enabled
        val res = fenceInfoService.updateById(fenceInfo)
        fenceListCache.invalidate(fenceInfo.mapId)
        return okOf(res)
    }
}
