package com.lgguan.iot.position.service

import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.lgguan.iot.position.entity.GatewayInfo
import com.lgguan.iot.position.mapper.IGatewayInfoMapper
import org.apache.commons.collections.CollectionUtils
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 *
 *
 * @author N.Liu
 **/
interface IGatewayInfoService: IService<GatewayInfo> {
    fun batchUpdate(beaconInfos: List<GatewayInfo>): Boolean
}

@Service
class GatewayInfoServiceImpl: IGatewayInfoService, ServiceImpl<IGatewayInfoMapper, GatewayInfo>() {

    @Transactional
    @Async("iotTaskExecutor")
    override fun batchUpdate(gatewayInfos: List<GatewayInfo>): Boolean {
        if(CollectionUtils.isEmpty(gatewayInfos)) return false
        return this.updateBatchById(gatewayInfos)
    }
}
