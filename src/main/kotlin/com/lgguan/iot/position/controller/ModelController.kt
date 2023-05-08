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
    @PutMapping("/model/{modelId}")
    fun updateModel(
        @PathVariable modelId: String,
        @Valid @RequestBody updateModel: AddOrUpdateModel
    ): RestValue<Boolean> {
        return modelService.updateModel(modelId, updateModel)
    }

    @Operation(summary = "删除模型")
    @DeleteMapping("/model/{modelId}")
    fun deleteModel(@PathVariable modelId: String): RestValue<Boolean> {
        return modelService.deleteModel(modelId)
    }

    @Operation(summary = "操作模型有效/无效")
    @PutMapping("/model/{modelId}/{active}")
    fun updateModelActive(
        @PathVariable modelId: String,
        @PathVariable active: Boolean
    ): RestValue<Boolean> {
        return modelService.updateModelActive(modelId, active)
    }

    @Operation(summary = "获取设备所属模型")
    @PostMapping("/model/device")
    fun getModelInfoByDeviceId(@Valid @RequestBody authorityBody: IotAuthorityRequest): RestValue<IotAuthorityResponse> {
        return modelManageService.getModelInfoByDeviceId(authorityBody)
    }
}