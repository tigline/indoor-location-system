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

fun getRole() = tokenCache.getIfPresent(getToken())?.role
