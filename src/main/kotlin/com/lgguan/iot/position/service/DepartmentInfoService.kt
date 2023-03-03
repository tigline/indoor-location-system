package com.lgguan.iot.position.service

import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.lgguan.iot.position.entity.DepartmentInfo
import com.lgguan.iot.position.mapper.IDepartmentInfoMapper
import org.springframework.stereotype.Service

/**
 *
 *
 * @author N.Liu
 **/
interface IDepartmentInfoService: IService<DepartmentInfo>

@Service
class DepartmentInfoServiceImpl: IDepartmentInfoService, ServiceImpl<IDepartmentInfoMapper, DepartmentInfo>()
