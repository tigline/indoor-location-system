package com.lgguan.iot.position.bean

import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

data class AddOrUpdateModelDevice (
    var id: String? = null,
    @field:Schema(required = true)
    var deviceId: String? = null,
    var companyId: Int? = null,
    @field:Schema(required = true)
    val modelId: String? = null,
    var active: Boolean? = false,
    var createTime: Date? = null,
    var updateTime: Date? = null
)