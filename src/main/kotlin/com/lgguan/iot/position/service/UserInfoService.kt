package com.lgguan.iot.position.service

import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.lgguan.iot.position.entity.UserInfo
import com.lgguan.iot.position.mapper.IUserInfoMapper
import org.springframework.stereotype.Service

/**
 *
 *
 * @author N.Liu
 **/
interface IUserInfoService: IService<UserInfo>

@Service
class UserInfoServiceImpl : IUserInfoService, ServiceImpl<IUserInfoMapper, UserInfo>() {

}
