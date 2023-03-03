package com.lgguan.iot.position.controller

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.lgguan.iot.position.bean.*
import com.lgguan.iot.position.entity.UserInfo
import com.lgguan.iot.position.service.IUserInfoService
import com.lgguan.iot.position.util.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

/**
 *
 *
 * @author N.Liu
 **/
@Tag(name = "登录相关")
@RestController
@RequestMapping("/api/v1")
class LoginController(
    val userInfoService: IUserInfoService,
    val passwordEncoder: PasswordEncoder
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Operation(
        summary = "注册", parameters = [Parameter(
            name = "Authorization", required = false, `in` = ParameterIn.HEADER, hidden = true
        )]
    )
    @PostMapping("/register")
    fun register(@Valid @RequestBody registryInfo: RegistryInfo): RestValue<Boolean> {
        val count = userInfoService.count(
            KtQueryWrapper(UserInfo::class.java)
                .eq(UserInfo::username, registryInfo.username)
        )
        if (count > 0) {
            return failedOf(IErrorCode.UserExists)
        }
        val userInfo = UserInfo().apply {
            username = registryInfo.username
            password = passwordEncoder.encode(registryInfo.password)
            nickname = registryInfo.nickname
            email = registryInfo.email
            phone = registryInfo.phone
            role = RoleType.User
        }
        val save = userInfoService.save(userInfo)
        return if (save) okOf(true) else failedOf()
    }

    @Operation(summary = "登录", parameters = [Parameter(
        name = "Authorization", required = false, `in` = ParameterIn.HEADER, hidden = true
    )])
    @PostMapping("/login")
    fun login(@RequestBody loginInfo: LoginInfo): RestValue<TokenInfo> {
        log.info("User login")
        val userInfo: UserInfo? = userInfoService.getOne(
            KtQueryWrapper(UserInfo::class.java)
                .eq(UserInfo::username, loginInfo.username)
        )
        if (userInfo == null || !passwordEncoder.matches(loginInfo.password, userInfo.password)) {
            return failedOf(IErrorCode.UsernamePasswordIncorrect)
        }
        val claimMap = ClaimMap(userInfo.userId!!, userInfo.role!!.name)
        val accessToken = claimMap.createToken(TokenType.Access)
        val refreshToken = claimMap.createToken(TokenType.Refresh)
        val tokenInfo = TokenInfo(accessToken.token, accessToken.expired, refreshToken.token, refreshToken.expired)
        tokenCache.put(accessToken.token, userInfo)
        return okOf(tokenInfo)
    }

    @Operation(summary = "刷新token", parameters = [Parameter(
        name = "Authorization", required = false, `in` = ParameterIn.HEADER, hidden = true
    )])
    @PostMapping("/refreshToken")
    fun refreshToken(refreshToken: String): RestValue<TokenInfo> {
        log.info("Refresh token")
        val tokenPair = refreshToken.refresh()
        val tokenInfo = TokenInfo(
            accessToken = tokenPair.first.token, accessExpired = tokenPair.first.expired,
            refreshToken = tokenPair.second.token, refreshExpired = tokenPair.second.expired
        )
        val userId = tokenInfo.accessToken.getValueOfKey("userId")!!
        val userInfo = userInfoService.getById(userId)
        tokenCache.put(tokenInfo.accessToken, userInfo)
        return okOf(tokenInfo)
    }

}
