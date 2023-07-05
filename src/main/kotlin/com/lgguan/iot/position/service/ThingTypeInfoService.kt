package com.lgguan.iot.position.service

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.lgguan.iot.position.bean.IErrorCode
import com.lgguan.iot.position.bean.failedOf
import com.lgguan.iot.position.entity.ThingTypeInfo
import com.lgguan.iot.position.entity.UserInfo
import com.lgguan.iot.position.mapper.IThingTypeInfoMapper
import org.springframework.stereotype.Service

/**
 *
 *
 * @author N.Liu
 **/
interface IThingTypeInfoService: IService<ThingTypeInfo>

@Service
class ThingTypeInfoServiceImpl: IThingTypeInfoService, ServiceImpl<IThingTypeInfoMapper, ThingTypeInfo>() {
//    fun typeNameExists(typeName: String): Any {
//        return baseMapper.selectCountByTypeName(typeName) > 0
//
//
//    }
//    override fun save(entity: ThingTypeInfo): Boolean {
//        if (!typeNameExists(entity.typeName ?:"")) {
//            return super<ServiceImpl>.save(entity)
//        }
//        throw IllegalArgumentException("typeName ${entity.typeName} already exists")
//    }
}


