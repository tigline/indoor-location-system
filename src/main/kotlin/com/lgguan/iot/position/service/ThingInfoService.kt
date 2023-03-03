package com.lgguan.iot.position.service

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.lgguan.iot.position.entity.ThingInfo
import com.lgguan.iot.position.mapper.IThingInfoMapper
import org.springframework.stereotype.Service

/**
 *
 *
 * @author N.Liu
 **/
interface IThingInfoService : IService<ThingInfo> {
    fun removeTag(tag: String)
}

@Service
class ThingInfoServiceImpl : IThingInfoService, ServiceImpl<IThingInfoMapper, ThingInfo>() {
    override fun removeTag(tag: String) {
        this.remove(KtQueryWrapper(ThingInfo::class.java).eq(ThingInfo::tag, tag))
    }
}
