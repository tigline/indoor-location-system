package com.lgguan.iot.position.bean

import io.swagger.v3.oas.annotations.media.Schema

data class IotCompanyRequest(
    @field:Schema(required = true)
    val companyCode: String,
    @field:Schema(required = true)
    val companyName: Map<String, Object>
)