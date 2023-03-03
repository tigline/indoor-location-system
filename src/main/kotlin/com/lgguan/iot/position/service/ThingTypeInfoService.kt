package com.lgguan.iot.position.service

import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.lgguan.iot.position.entity.ThingTypeInfo
import com.lgguan.iot.position.mapper.IThingTypeInfoMapper
import org.springframework.stereotype.Service

/**
 *
 *
 * @author N.Liu
 **/
interface IThingTypeInfoService: IService<ThingTypeInfo>

@Service
class ThingTypeInfoServiceImpl: IThingTypeInfoService, ServiceImpl<IThingTypeInfoMapper, ThingTypeInfo>()
