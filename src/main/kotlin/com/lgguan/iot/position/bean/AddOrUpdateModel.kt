package com.lgguan.iot.position.bean

import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

data class AddOrUpdateModel (
    var modelId: String? = null,
    @field:Schema(required = true)
    var modelName: String? = null,
    var modelVersion: String? = null,
    var companyId: Int? = null,
    val properties: String? = null,
    var configs: String? = null,
    var events: String? = null,
    var commands: String? = null,
    var active: Boolean? = false,
    var createTime: Date? = null,
    var updateTime: Date? = null
)