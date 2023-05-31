package com.lgguan.iot.position.controller

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.lgguan.iot.position.bean.*
import com.lgguan.iot.position.entity.ThingInfo
import com.lgguan.iot.position.entity.ThingTypeInfo
import com.lgguan.iot.position.service.ThingManageService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springdoc.core.annotations.ParameterObject
import org.springframework.web.bind.annotation.*

/**
 *
 *
 * @author N.Liu
 **/
@Tag(name = "物品管理")
@RestController
@RequestMapping("/api/v1")
class ThingManageController(val thingManageService: ThingManageService) {
    @Operation(summary = "分页获取物品分类")
    @GetMapping("/thingTypes")
    fun pageThingType(hidePicture: Boolean? = false, @ParameterObject pageLimit: PageLimit):
            RestValue<PageResult<ThingTypeInfo>> {
        val res = thingManageService.pageThingTypeInfos(hidePicture ?: false, pageLimit)
        return okOf(res)
    }

    @Operation(summary = "新增物品分类")
    @PostMapping("/thingTypes")
    fun addThingType(@Valid @RequestBody addThingType: AddOrUpdateThingType): RestValue<Boolean> {

        val count = thingManageService.thingTypeInfoService.count(
            KtQueryWrapper(ThingTypeInfo::class.java)
                .eq(ThingTypeInfo::typeName, addThingType.typeName)
        )
        if (count > 0) {
            return failedOf(IErrorCode.DataExists)
        }

        val res = thingManageService.addThingTypeInfo(addThingType)
        return okOf(res)
    }

    @Operation(summary = "修改物品分类")
    @PutMapping("/thingTypes/{typeId}")
    fun updateThingType(
        @PathVariable typeId: Long,
        @Valid @RequestBody updateThingType: AddOrUpdateThingType
    ): RestValue<Boolean> {
        return thingManageService.updateThingTypeInfo(typeId, updateThingType)
    }

    @Operation(summary = "删除物品分类")
    @DeleteMapping("/thingTypes/{typeId}")
    fun deleteThingType(@PathVariable typeId: Long): RestValue<Boolean> {

        var count = thingManageService.thingInfoService.count(
            KtQueryWrapper(ThingInfo::class.java)
                .eq(ThingTypeInfo::typeId, typeId)
        )

        if (count > 0) {
            return failedOf(IErrorCode.TypeIdIsReferenced)
        }

        return thingManageService.deleteThingTypeInfo(typeId)
    }

    @Operation(summary = "分页获取物品")
    @GetMapping("/things")
    fun pageThing(name: String? = null, @ParameterObject pageLimit: PageLimit): RestValue<PageResult<ThingInfo>> {
        val res = thingManageService.pageThingInfos(name, pageLimit)
        return okOf(res)
    }

    @Operation(summary = "新增物品")
    @PostMapping("/things")
    fun addThing(@Valid @RequestBody addThing: AddOrUpdateThing): RestValue<Boolean> {
        val res = thingManageService.addThingInfo(addThing)
        return okOf(res)
    }

    @Operation(summary = "修改物品")
    @PutMapping("/things/{thingId}")
    fun updateThing(
        @PathVariable thingId: Long,
        @Valid @RequestBody updateThing: AddOrUpdateThing
    ): RestValue<Boolean> {
        return thingManageService.updateThingInfo(thingId, updateThing)
    }

    @Operation(summary = "删除物品")
    @DeleteMapping("/things/{thingId}")
    fun deleteThing(@PathVariable thingId: Long): RestValue<Boolean> {
        return thingManageService.deleteThingInfo(thingId)
    }

    @Operation(summary = "解绑物品标签")
    @DeleteMapping("/things/{thingId}/{tag}")
    fun unbindTag(@PathVariable thingId: Long, @PathVariable tag: String): RestValue<Boolean> {
        return thingManageService.unbindTag(thingId, tag)
    }

}
