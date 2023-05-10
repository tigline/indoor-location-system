package com.lgguan.iot.position.controller

import com.lgguan.iot.position.bean.*
import com.lgguan.iot.position.service.ModelManageService
import com.lgguan.iot.position.service.ModelService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@Tag(name = "模型管理")
@RestController
@RequestMapping("/api/v1")
class ModelController(val modelService: ModelService, var modelManageService: ModelManageService) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Operation(summary = "添加模型")
    @PostMapping("/model")
    fun addModel(@Valid @RequestBody addModel: AddOrUpdateModel): RestValue<Boolean> {
        return modelService.addModel(addModel)
    }

    @Operation(summary = "编辑模型")
    @PutMapping("/model/{id}")
    fun updateModel(
        @PathVariable id: Int,
        @Valid @RequestBody updateModel: AddOrUpdateModel
    ): RestValue<Boolean> {
        return modelService.updateModel(id, updateModel)
    }

    @Operation(summary = "删除模型")
    @DeleteMapping("/model/{id}")
    fun deleteModel(@PathVariable id: Int): RestValue<Boolean> {
        return modelService.deleteModel(id)
    }

    @Operation(summary = "操作模型有效/无效")
    @PutMapping("/model/{id}/{active}")
    fun updateModelActive(
        @PathVariable id: Int,
        @PathVariable active: Boolean
    ): RestValue<Boolean> {
        return modelService.updateModelActive(id, active)
    }

    @Operation(summary = "获取设备所属模型")
    @PostMapping("/model/device")
    fun getModelInfoByDeviceId(@Valid @RequestBody authorityBody: IotAuthorityRequest): RestValue<IotAuthorityResponse> {
        return modelManageService.getModelInfoByClientId(authorityBody)
    }
}