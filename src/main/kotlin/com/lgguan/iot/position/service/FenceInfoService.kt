package com.lgguan.iot.position.service

import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.lgguan.iot.position.bean.FenceAndMapInfo
import com.lgguan.iot.position.bean.QueryFenceAndMapParam
import com.lgguan.iot.position.entity.FenceInfo
import com.lgguan.iot.position.mapper.IFenceInfoMapper
import org.springframework.stereotype.Service

/**
 *
 *
 * @author N.Liu
 **/
interface IFenceInfoService: IService<FenceInfo> {
    fun pageFenceAndMap(page: IPage<FenceInfo>, param: QueryFenceAndMapParam): IPage<FenceAndMapInfo>
}

@Service
class FenceInfoServiceImpl: IFenceInfoService, ServiceImpl<IFenceInfoMapper, FenceInfo>() {
    override fun pageFenceAndMap(page: IPage<FenceInfo>, param: QueryFenceAndMapParam): IPage<FenceAndMapInfo> {
        return this.getBaseMapper().pageFenceAndMap(page, param)
    }
}
