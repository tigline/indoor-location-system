package com.lgguan.iot.position.service

import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.lgguan.iot.position.entity.AlarmInfo
import com.lgguan.iot.position.mapper.IAlarmInfoMapper
import org.springframework.stereotype.Service

/**
 *
 *
 * @author N.Liu
 **/
interface IAlarmInfoService : IService<AlarmInfo> {
}

@Service
class AlarmInfoServiceImpl : IAlarmInfoService, ServiceImpl<IAlarmInfoMapper, AlarmInfo>() {
}
