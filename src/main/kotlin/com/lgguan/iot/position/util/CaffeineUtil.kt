package com.lgguan.iot.position.util

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.lgguan.iot.position.entity.FenceInfo
import com.lgguan.iot.position.entity.UserInfo
import java.time.Duration
import java.util.concurrent.TimeUnit

val tokenCache by lazy<Cache<String, UserInfo>> {
    Caffeine.newBuilder()
        .expireAfterWrite(Duration.ofSeconds(TokenType.Access.expireTime()))
        .maximumSize(100)
        .build()
}

val fenceListCache by lazy<Cache<String, List<FenceInfo>>> {
    Caffeine.newBuilder()
        .expireAfterWrite(5, TimeUnit.HOURS)
        .expireAfterAccess(24, TimeUnit.HOURS)
        .build()
}
