package com.lgguan.iot.position.service

import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.lgguan.iot.position.entity.AoaDataInfo
import com.lgguan.iot.position.mapper.IAoaDataInfoMapper
import org.apache.commons.collections.CollectionUtils
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 *
 *
 * @author N.Liu
 **/
interface IAoaDataInfoService: IService<AoaDataInfo> {
    fun batchInsert(aoaDataInfos: List<AoaDataInfo>): Boolean
}

@Service
class AoaDataInfoServiceImpl : IAoaDataInfoService, ServiceImpl<IAoaDataInfoMapper, AoaDataInfo>() {

    @Transactional
    @Async("iotTaskExecutor")
    override fun batchInsert(aoaDataInfos: List<AoaDataInfo>): Boolean {
        if(CollectionUtils.isEmpty(aoaDataInfos)) return false
        return this.saveBatch(aoaDataInfos)
    }
}
