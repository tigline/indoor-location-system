package com.lgguan.iot.position.controller

import com.lgguan.iot.position.bean.*
import com.lgguan.iot.position.entity.AoaDataInfo
import com.lgguan.iot.position.entity.BeaconInfo
import com.lgguan.iot.position.entity.GatewayInfo
import com.lgguan.iot.position.service.DeviceManageService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springdoc.core.annotations.ParameterObject
import org.springframework.web.bind.annotation.*

/**
 *
 *
 * @author N.Liu
 **/
@Tag(name = "设备管理")
@RestController
@RequestMapping("/api/v1")
class DeviceManageController(val deviceManageService: DeviceManageService) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Operation(summary = "分页获取网关列表")
    @GetMapping("/gateway")
    fun pageGateway(@ParameterObject param: QueryGatewayParam, @ParameterObject pageLimit: PageLimit):
            RestValue<PageResult<GatewayInfo>> {
        log.info("Page gateway info list")
        val pageResult = deviceManageService.pageGatewayInfos(param, pageLimit)
        return okOf(pageResult)
    }

    @Operation(summary = "添加网关信息")
    @PostMapping("/gateway")
    fun addGateway(@RequestBody addGatewayInfo: AddGatewayInfo): RestValue<Boolean> {
        log.info("Add gateway info")
        return deviceManageService.addGatewayInfo(addGatewayInfo)
    }

    @Operation(summary = "编辑网关信息")
    @PutMapping("/gateway{gateway}")
    fun updateGateway(
        @PathVariable gateway: String,
        @Valid @RequestBody updateGateway: UpdateGateway
    ): RestValue<Boolean> {
        return deviceManageService.updateGatewayInfo(gateway, updateGateway)
    }

    @Operation(summary = "删除网关")
    @DeleteMapping("/gateway/{gateway}")
    fun deleteGateway(@PathVariable gateway: String): RestValue<Boolean> {
        return deviceManageService.deleteGatewayInfo(gateway)
    }

    @Operation(summary = "获取网关在线状态")
    @GetMapping("/status/gateway")
    fun getGatewayOnlineStatusCount(mapId: String? = null): RestValue<OnlineStatusCount> {
        val res = deviceManageService.getGatewayOnlineStatus(mapId)
        return okOf(res)
    }

    @Operation(summary = "分页获取标签列表")
    @GetMapping("/beacon")
    fun pageBeacon(
        @ParameterObject param: QueryBeaconParam,
        @ParameterObject pageLimit: PageLimit
    ): RestValue<PageResult<BeaconInfo>> {
        log.info("Page beacon info list")
        val pageResult = deviceManageService.pageBeaconInfos(param, pageLimit)
        return okOf(pageResult)
    }

    @Operation(summary = "添加标签信息")
    @PostMapping("/beacon")
    fun addBeacon(@RequestBody addBeaconInfo: AddBeaconInfo): RestValue<Boolean> {
        log.info("Add beacon info")
        return deviceManageService.addBeaconInfo(addBeaconInfo)
    }

    @Operation(summary = "修改标签信息")
    @PutMapping("/beacon/{deviceId}")
    fun updateBeacon(
        @PathVariable deviceId: String,
        @Valid @RequestBody updateBeacon: UpdateBeacon
    ): RestValue<Boolean> {
        return deviceManageService.updateBeaconInfo(deviceId, updateBeacon)
    }

    @Operation(summary = "删除标签")
    @DeleteMapping("/beacon/{deviceId}")
    fun deleteBeacon(@PathVariable deviceId: String): RestValue<Boolean> {
        return deviceManageService.deleteBeaconInfo(deviceId)
    }

    @Operation(summary = "获取未绑定标签列表")
    @GetMapping("/unbound/beacon")
    fun listUnboundBeacon(type: BeaconType? = null): RestValue<List<BeaconInfo>> {
        val res = deviceManageService.listUnboundBeaconInfo(type)
        return okOf(res)
    }

    @Operation(summary = "获取标签轨迹")
    @GetMapping("/location")
    fun listBeaconLocation(@Valid @ParameterObject param: QueryBeaconLocationParam): RestValue<List<AoaDataInfo>> {
        val location = deviceManageService.listBeaconLocationWithMovingAverage(param)
        return okOf(location)
    }

    @Operation(summary = "不同标签类型的在线状态")
    @GetMapping("/status/beacon")
    fun getBeaconStatusCounts(mapId: String? = null, type: BeaconType? = null, onlyOnline: Boolean? = null):
            RestValue<MutableMap<BeaconType, OnlineStatusCount>> {
        val res = deviceManageService.getBeaconOnlineStatusCounts(mapId, type, onlyOnline)
        return okOf(res)
    }
}
