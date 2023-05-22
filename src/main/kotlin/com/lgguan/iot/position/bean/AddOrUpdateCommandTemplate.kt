package com.lgguan.iot.position.bean

import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

data class AddOrUpdateCommandTemplate (
    var id: Int? = null,
    @field:Schema(required = true)
    var name: String? = null,
    var title: String? = null,
    var companyId: Int? = null,
    var modelId: Int? = null,
    var content: String? = null,
    var param: String? = null,
    var ackCheck: Boolean? = false,
    var enableCheck: Boolean? = true,
    var category: String? = null,
    var onVersion: Int? = null,
    var description: String? = null,
    var active: Boolean? = true,
    var createTime: Date? = null,
    var updateTime: Date? = null
)