package com.lgguan.iot.position.bean

import io.swagger.v3.oas.annotations.media.Schema

data class IotAuthorityRequest(
    @field:Schema(required = true)
    val deviceId: String,
    @field:Schema(required = true)
    val userName: String,
    @field:Schema(required = true)
    val passWord: String
)