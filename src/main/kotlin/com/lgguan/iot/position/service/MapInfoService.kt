package com.lgguan.iot.position.service

import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.lgguan.iot.position.entity.MapInfo
import com.lgguan.iot.position.mapper.IMapInfoMapper
import org.springframework.stereotype.Service

/**
 *
 *
 * @author N.Liu
 **/
interface IMapInfoService: IService<MapInfo>

@Service
class MapInfoServiceImpl: IMapInfoService, ServiceImpl<IMapInfoMapper, MapInfo>()
