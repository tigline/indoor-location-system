package com.lgguan.iot.position.bean

/**
 *
 *
 * @author N.Liu
 **/
data class TokenInfo(
    val accessToken: String,
    val accessExpired: Long,
    val refreshToken: String,
    val refreshExpired: Long
)
