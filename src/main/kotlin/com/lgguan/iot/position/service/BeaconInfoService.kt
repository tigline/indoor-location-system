package com.lgguan.iot.position.service

import com.baomidou.mybatisplus.extension.kotlin.KtUpdateWrapper
import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.lgguan.iot.position.bean.BindStatus
import com.lgguan.iot.position.entity.BeaconInfo
import com.lgguan.iot.position.mapper.IBeaconInfoMapper
import org.apache.commons.collections.CollectionUtils
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 *
 *
 * @author N.Liu
 **/
interface IBeaconInfoService : IService<BeaconInfo> {
    fun changeBindStatus(deviceId: String, status: BindStatus)
    fun getBoundNameByDeviceId(deviceId: String): String?

    fun batchUpdate(beaconInfos: List<BeaconInfo>): Boolean
}

@Service
class BeaconInfoServiceImpl : IBeaconInfoService, ServiceImpl<IBeaconInfoMapper, BeaconInfo>() {
    override fun changeBindStatus(deviceId: String, status: BindStatus) {
        this.update(
            KtUpdateWrapper(BeaconInfo::class.java)
                .eq(BeaconInfo::deviceId, deviceId)
                .set(BeaconInfo::status, status)
        )
    }

    override fun getBoundNameByDeviceId(deviceId: String): String? {
        return this.getBaseMapper().getBoundNameByDeviceId(deviceId)
    }

    @Transactional
    @Async("iotTaskExecutor")
    override fun batchUpdate(beaconInfos: List<BeaconInfo>): Boolean {
        if(CollectionUtils.isEmpty(beaconInfos)) return false
        return this.updateBatchById(beaconInfos)
    }
}
