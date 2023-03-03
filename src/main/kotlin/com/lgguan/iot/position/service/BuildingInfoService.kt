package com.lgguan.iot.position.service

import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.lgguan.iot.position.entity.BuildingInfo
import com.lgguan.iot.position.mapper.IBuildingInfoMapper
import org.springframework.stereotype.Service

/**
 *
 *
 * @author N.Liu
 **/
interface IBuildingInfoService: IService<BuildingInfo>

@Service
class BuildingInfoServiceImpl: IBuildingInfoService, ServiceImpl<IBuildingInfoMapper, BuildingInfo>()
