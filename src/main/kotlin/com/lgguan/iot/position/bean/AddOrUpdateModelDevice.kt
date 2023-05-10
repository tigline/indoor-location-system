package com.lgguan.iot.position.bean

import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

data class AddOrUpdateModelDevice (
    var id: Int? = null,
    @field:Schema(required = true)
    var deviceId: String? = null,
    var clientId: String? = null,
    @field:Schema(required = true)
    var companyId: Int? = null,
    @field:Schema(required = true)
    val modelId: Int? = null,
    var active: Boolean? = false,
    var createTime: Date? = null,
    var updateTime: Date? = null
)