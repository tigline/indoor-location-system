package com.lgguan.iot.position.controller

import com.lgguan.iot.position.aop.HasPermission
import com.lgguan.iot.position.bean.*
import com.lgguan.iot.position.entity.UserInfo
import com.lgguan.iot.position.service.UserManageService
import com.lgguan.iot.position.util.getToken
import com.lgguan.iot.position.util.tokenCache
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springdoc.core.annotations.ParameterObject
import org.springframework.web.bind.annotation.*

@Tag(name = "用户管理")
@RestController
@RequestMapping("/api/v1")
class UserManageController(val userManageService: UserManageService) {
    @Operation(summary = "获取用户信息")
    @GetMapping("/userInfo")
    fun getUserInfo(): RestValue<UserInfo> {
        val token = getToken()
        val oldUserInfo = tokenCache.getIfPresent(token)
        val userInfo = userManageService.getUserInfo(oldUserInfo!!.userId!!)
        return if (userInfo != null) okOf(userInfo) else failedOf(IErrorCode.DataNotExists)
    }

    @Operation(summary = "分页获取用户列表")
    @GetMapping("/users")
    fun pageUser(@ParameterObject param: QueryUserParam, @ParameterObject pageLimit: PageLimit):RestValue<PageResult<UserInfo>> {
        val userInfos = userManageService.pageUserInfos(param, pageLimit)
        return okOf(userInfos)
    }

    @Operation(summary = "更新用户信息")
    @PutMapping("/userInfo/{userId}")
    fun updateUserInfo(@PathVariable userId: String, @RequestBody updateUserInfo: UpdateUserInfo): RestValue<Boolean> {
        return userManageService.updateUserInfoById(userId, updateUserInfo)
    }

    @Operation(summary = "更改用户角色")
    @PutMapping("/user/role")
    @HasPermission([RoleType.SuperAdmin])
    fun updateUserRole(@Valid @RequestBody updateUserRole: UpdateUserRole): RestValue<Boolean> {
        return userManageService.updateUserRole(updateUserRole.userId, RoleType.valueOf(updateUserRole.role))
    }
}
