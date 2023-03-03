package com.lgguan.iot.position.service

import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.lgguan.iot.position.entity.GatewayInfo
import com.lgguan.iot.position.mapper.IGatewayInfoMapper
import org.springframework.stereotype.Service

/**
 *
 *
 * @author N.Liu
 **/
interface IGatewayInfoService: IService<GatewayInfo>

@Service
class GatewayInfoServiceImpl: IGatewayInfoService, ServiceImpl<IGatewayInfoMapper, GatewayInfo>()
