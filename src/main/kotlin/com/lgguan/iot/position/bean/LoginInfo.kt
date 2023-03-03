package com.lgguan.iot.position.bean

import io.swagger.v3.oas.annotations.media.Schema

data class LoginInfo(
    @field:Schema(required = true)
    val username: String,
    @field:Schema(required = true)
    val password: String
)
