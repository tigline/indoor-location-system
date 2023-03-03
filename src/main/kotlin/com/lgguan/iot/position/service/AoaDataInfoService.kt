package com.lgguan.iot.position.service

import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.lgguan.iot.position.entity.AoaDataInfo
import com.lgguan.iot.position.mapper.IAoaDataInfoMapper
import org.springframework.stereotype.Service

/**
 *
 *
 * @author N.Liu
 **/
interface IAoaDataInfoService: IService<AoaDataInfo>

@Service
class AoaDataInfoServiceImpl : IAoaDataInfoService, ServiceImpl<IAoaDataInfoMapper, AoaDataInfo>() {

}
