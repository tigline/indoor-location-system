package com.lgguan.iot.position.controller

import com.lgguan.iot.position.bean.*
import com.lgguan.iot.position.entity.BuildingInfo
import com.lgguan.iot.position.entity.FenceInfo
import com.lgguan.iot.position.entity.MapInfo
import com.lgguan.iot.position.service.SystemManageService
import com.lgguan.iot.position.systemRunningTime
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springdoc.core.annotations.ParameterObject
import org.springframework.web.bind.annotation.*

@Tag(name = "系统管理")
@RestController
@RequestMapping("/api/v1")
class SystemManageController(val systemManageService: SystemManageService) {

    @Operation(summary = "获取建筑列表")
    @GetMapping("/buildings")
    fun listBuilding(name: String? = null): RestValue<List<BuildingInfo>> {
        val res = systemManageService.listBuildingInfos(name)
        return okOf(res)
    }

    @Operation(summary = "添加建筑")
    @PostMapping("/buildings")
    fun addBuilding(@Valid @RequestBody addBuilding: AddOrUpdateBuilding): RestValue<Boolean> {
        val res = systemManageService.addBuildingInfo(addBuilding)
        return okOf(res)
    }

    @Operation(summary = "编辑建筑")
    @PutMapping("/buildings/{buildingId}")
    fun updateBuilding(
        @PathVariable buildingId: String,
        @Valid @RequestBody updateBuilding: AddOrUpdateBuilding
    ): RestValue<Boolean> {
        return systemManageService.updateBuildingInfo(buildingId, updateBuilding)
    }

    @Operation(summary = "删除建筑")
    @DeleteMapping("/buildings/{buildingId}")
    fun deleteBuilding(@PathVariable buildingId: String): RestValue<Boolean> {
        return systemManageService.deleteBuildingInfo(buildingId)
    }

    @Operation(summary = "获取地图列表")
    @GetMapping("/maps")
    fun listMaps(@ParameterObject param: QueryMapParam): RestValue<List<MapInfo>> {
        val mapInfos = systemManageService.listMapInfos(param)
        return okOf(mapInfos)
    }

    @Operation(summary = "获取单个地图信息")
    @GetMapping("/maps/{mapId}")
    fun getMap(@PathVariable mapId: String): RestValue<MapInfo> {
        return systemManageService.getMapInfo(mapId)
    }

    @Operation(summary = "添加地图")
    @PostMapping("/maps")
    fun addMap(@Valid @RequestBody addOrUpdateMapInfo: AddOrUpdateMapInfo): RestValue<Boolean> {
        val res = systemManageService.addMapInfo(addOrUpdateMapInfo)
        return okOf(res)
    }

    @Operation(summary = "编辑地图信息")
    @PostMapping("/maps/{mapId}")
    fun updateMap(
        @PathVariable mapId: String,
        @Valid @RequestBody addOrUpdateMapInfo: AddOrUpdateMapInfo
    ): RestValue<Boolean> {
        return systemManageService.updateMapInfo(mapId, addOrUpdateMapInfo)
    }

    @Operation(summary = "删除地图")
    @DeleteMapping("/maps/{mapId}")
    fun deleteMap(@PathVariable mapId: String): RestValue<Boolean> {
        return systemManageService.deleteMapInfo(mapId)
    }

    @Operation(summary = "分页获取电子围栏列表")
    @GetMapping("/fences")
    fun pageFence(@ParameterObject param: QueryFenceAndMapParam, @ParameterObject pageLimit: PageLimit):
            RestValue<PageResult<FenceAndMapInfo>> {
        val pageResult = systemManageService.pageFenceAndMap(param, pageLimit)
        return okOf(pageResult)
    }

    @Operation(summary = "获取指定围栏信息")
    @GetMapping("/fences/{fenceId}")
    fun getFence(@PathVariable fenceId: String): RestValue<FenceInfo> {
        return systemManageService.getFenceInfoById(fenceId)
    }

    @Operation(summary = "添加地理围栏")
    @PostMapping("/fences")
    fun addFence(@Valid @RequestBody addFenceInfo: AddOrUpdateFenceInfo): RestValue<Boolean> {
        val res = systemManageService.addFenceInfo(addFenceInfo)
        return okOf(res)
    }

    @Operation(summary = "编辑地理围栏")
    @PostMapping("/fences/{fenceId}")
    fun updateFence(
        @PathVariable fenceId: String,
        @Valid @RequestBody updateFenceInfo: AddOrUpdateFenceInfo
    ): RestValue<Boolean> {
        return systemManageService.updateFenceInfo(fenceId, updateFenceInfo)
    }

    @Operation(summary = "删除地理围栏")
    @DeleteMapping("/fences/{fenceId}")
    fun deleteFence(@PathVariable fenceId: String): RestValue<Boolean> {
        return systemManageService.deleteFenceInfo(fenceId)
    }

    @Operation(summary = "切换围栏状态")
    @PutMapping("/fences/{fenceId}/switch")
    fun switchFenceStatus(@PathVariable fenceId: String): RestValue<Boolean> {
        return systemManageService.changeFenceStatus(fenceId)
    }

    @Operation(summary = "获取系统开始运行时间")
    @GetMapping("/runningTime")
    fun getSystemRunningTime():RestValue<Long> {
        return okOf(systemRunningTime)
    }
}
