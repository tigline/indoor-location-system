package com.lgguan.iot.position.service

import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.lgguan.iot.position.entity.PersonnelTypeInfo
import com.lgguan.iot.position.mapper.IPersonnelTypeInfoMapper
import org.springframework.stereotype.Service

/**
 *
 *
 * @author N.Liu
 **/
interface IPersonnelTypeService: IService<PersonnelTypeInfo>

@Service
class PersonnelTypeServiceImpl: IPersonnelTypeService, ServiceImpl<IPersonnelTypeInfoMapper, PersonnelTypeInfo>()
