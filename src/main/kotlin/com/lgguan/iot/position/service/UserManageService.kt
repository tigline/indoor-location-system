package com.lgguan.iot.position.service

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.lgguan.iot.position.bean.*
import com.lgguan.iot.position.entity.UserInfo
import com.lgguan.iot.position.ext.convert
import com.lgguan.iot.position.util.getToken
import com.lgguan.iot.position.util.tokenCache
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(rollbackFor = [Exception::class])
class UserManageService(val userInfoService: IUserInfoService) {
    fun getUserInfo(userId: String): UserInfo? {
        val userInfo: UserInfo? = userInfoService.getById(userId)
        userInfo?.password = null
        return userInfo
    }

    fun updateUserInfoById(userId: String, updateUserInfo: UpdateUserInfo): RestValue<Boolean> {
        val userInfo = userInfoService.getById(userId)
            ?: return failedOf(IErrorCode.DataNotExists, "UserId [$userId] not exists")
        userInfo.nickname = updateUserInfo.nickname
        userInfo.email = updateUserInfo.email
        userInfo.phone = updateUserInfo.phone
        val update = userInfoService.updateById(userInfo)
        if (update) {
            val token = getToken()
            tokenCache.put(token, userInfo)
        }
        return okOf(update)
    }

    fun pageUserInfos(param: QueryUserParam, pageLimit: PageLimit): PageResult<UserInfo> {
        return userInfoService.page(pageLimit.convert(), KtQueryWrapper(UserInfo::class.java)
            .select(UserInfo::class.java) { u -> u.property != "password" }
            .like(!param.username.isNullOrEmpty(), UserInfo::username, param.username)
            .like(!param.nickname.isNullOrEmpty(), UserInfo::nickname, param.nickname))
            .convert()
    }

    fun updateUserRole(userId: String, role: RoleType): RestValue<Boolean> {
        val userInfo = userInfoService.getById(userId)
            ?: return failedOf(IErrorCode.DataNotExists, "UserId [$userId] not exists")
        userInfo.role = role
        val update = userInfoService.updateById(userInfo)
        return okOf(update)
    }
}
