package com.lgguan.iot.position.bean

import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

data class AddOrUpdateModel (
    var id: Int? = null,
    @field:Schema(required = true)
    var modelCode: String? = null,
    var modelName: String? = null,
    var versionName: String? = null,
    var versionCode: Int? = null,
    var companyId: Int? = null,
    val properties: String? = null,
    var configs: String? = null,
    var events: String? = null,
    var commands: String? = null,
    var active: Boolean? = false,
    var createTime: Date? = null,
    var updateTime: Date? = null
)