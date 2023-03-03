package com.lgguan.iot.position.service

import cn.hutool.core.date.DateUtil
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.lgguan.iot.position.bean.*
import com.lgguan.iot.position.entity.ThingInfo
import com.lgguan.iot.position.entity.ThingTypeInfo
import com.lgguan.iot.position.ext.convert
import org.springframework.stereotype.Service

/**
 *
 *
 * @author N.Liu
 **/
@Service
class ThingManageService(
    val thingTypeInfoService: IThingTypeInfoService,
    val thingInfoService: IThingInfoService,
    val beaconInfoService: IBeaconInfoService
) {
    fun pageThingTypeInfos(hidePicture: Boolean, pageLimit: PageLimit): PageResult<ThingTypeInfo> {
        return thingTypeInfoService.page(pageLimit.convert(), KtQueryWrapper(ThingTypeInfo::class.java)
            .select(ThingTypeInfo::class.java) { if (hidePicture) it.property != "picture" else true })
            .convert()
    }

    fun addThingTypeInfo(addThingType: AddOrUpdateThingType): Boolean {
        val typeInfo = ThingTypeInfo().apply {
            typeName = addThingType.typeName
            picture = addThingType.picture
            createTime = DateUtil.currentSeconds()
        }
        return thingTypeInfoService.save(typeInfo)
    }

    fun updateThingTypeInfo(typeId: Long, updateThingType: AddOrUpdateThingType): RestValue<Boolean> {
        val typeInfo = thingTypeInfoService.getById(typeId)
        typeInfo ?: return failedOf(IErrorCode.DataNotExists, "TypeId [$typeId] not exists")
        typeInfo.apply {
            typeName = updateThingType.typeName
            picture = updateThingType.picture
        }
        val res = thingTypeInfoService.updateById(typeInfo)
        return okOf(res)
    }

    fun deleteThingTypeInfo(typeId: Long): RestValue<Boolean> {
        thingTypeInfoService.getById(typeId) ?: return failedOf(IErrorCode.DataNotExists, "TypeId [$typeId] not exists")
        val res = thingTypeInfoService.removeById(typeId)
        return okOf(res)
    }

    fun pageThingInfos(name: String?, pageLimit: PageLimit): PageResult<ThingInfo> {
        return thingInfoService.page(
            pageLimit.convert(), KtQueryWrapper(ThingInfo::class.java)
                .like(!name.isNullOrEmpty(), ThingInfo::name, name)
        ).convert()
    }

    fun addThingInfo(addThing: AddOrUpdateThing): Boolean {
        val thingInfo = ThingInfo().apply {
            name = addThing.name
            tag = addThing.tag
            typeId = addThing.typeId
            picture = addThing.picture
        }
        if (!thingInfo.tag.isNullOrEmpty()) {
            beaconInfoService.changeBindStatus(thingInfo.tag!!, BindStatus.Bound)
        }
        return thingInfoService.save(thingInfo)
    }

    fun updateThingInfo(thingId: Long, updateThing: AddOrUpdateThing): RestValue<Boolean> {
        val thingInfo = thingInfoService.getById(thingId)
        thingInfo ?: return failedOf(IErrorCode.DataNotExists, "ThingId [$thingId] not exists")
        if (thingInfo.tag != updateThing.tag) {
            if (!thingInfo.tag.isNullOrEmpty()) {
                beaconInfoService.changeBindStatus(thingInfo.tag!!, BindStatus.Unbound)
            }
            if (!updateThing.tag.isNullOrEmpty()) {
                beaconInfoService.changeBindStatus(updateThing.tag, BindStatus.Bound)
            }
        }
        thingInfo.apply {
            name = updateThing.name
            tag = updateThing.tag
            typeId = updateThing.typeId
            picture = updateThing.picture
        }
        val res = thingInfoService.updateById(thingInfo)
        return okOf(res)
    }

    fun deleteThingInfo(thingId: Long): RestValue<Boolean> {
        val thingInfo = thingInfoService.getById(thingId)
        thingInfo ?: return failedOf(IErrorCode.DataNotExists, "ThingId [$thingId] not exists")
        if (!thingInfo.tag.isNullOrEmpty()) {
            beaconInfoService.changeBindStatus(thingInfo.tag!!, BindStatus.Unbound)
        }
        val res = thingInfoService.removeById(thingId)
        return okOf(res)
    }

    fun unbindTag(thingId: Long, tag: String): RestValue<Boolean> {
        val thingInfo = thingInfoService.getById(thingId)
        thingInfo ?: return failedOf(IErrorCode.DataNotExists, "ThingId [$thingId] not exists")
        beaconInfoService.changeBindStatus(tag, BindStatus.Unbound)
        thingInfo.tag = null
        val update = thingInfoService.updateById(thingInfo)
        return okOf(update)
    }
}
