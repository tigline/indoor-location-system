package com.lgguan.iot.position.bean

import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

data class AddOrUpdateCommandRecord(
    var id: Int? = null,
    @field:Schema(required = true)
    var clientId: String? = null,
    var templateId: Int? = null,
    var alias: String? = null,
    var content: String? = null,
    var param: String? = null,
    var immediately: Int? = null,
    var planExecuteTime: Date? = null,
    var timeout: Int? = null,
    var description: String? = null,
    var status: Int? = null,
    var executeEndTime: Date? = null,
    var creator: String? = null,
    var createTime: Date? = null,
    var updater: String? = null,
    var updateTime: Date? = null
)
