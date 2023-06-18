package com.lgguan.iot.position.bean

import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

data class AddOrUpdateCompany(
    var id: Int? = null,
    @field:Schema(required = true)
    var companyCode: String? = null,
    @field:Schema(required = true)
    var companyName: String? = null,
    var simpleName: String? = null,
    var contactName: String? = null,
    var contactPhone: String? = null,
    var beginCreateTime: Date? = null,
    var active: Boolean? = false,
    var createTime: Date? = null,
    var updateTime: Date? = null
)