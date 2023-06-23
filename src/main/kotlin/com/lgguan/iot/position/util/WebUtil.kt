package com.lgguan.iot.position.util

import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.*

/**
 *
 *
 * @author N.Liu
 **/
fun getRequest() =
    (Objects.requireNonNull(RequestContextHolder.getRequestAttributes()) as ServletRequestAttributes).request

fun HttpServletRequest.getToken() = this.getHeader("Authorization")?.replaceFirst(BEARER_PREFIX, "")
fun getToken() = getRequest().getToken()

fun HttpServletRequest.getApiKey(): String = this.getHeader("api_key")

fun getApiKey() = getRequest().getApiKey()

fun HttpServletRequest.getLanguage(): String = this.getHeader("lang")

fun getLanguage() = getRequest().getLanguage()

fun getRole() = tokenCache.getIfPresent(getToken())?.role
