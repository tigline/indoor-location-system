package com.lgguan.iot.position.bean

import com.lgguan.iot.position.entity.TopicInfo
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

data class AddOrUpdateModel (
    var id: Int? = null,
    @field:Schema(required = true)
    var vendor: String? = null,
    var jsonType: Int? = null,
    var topics: List<TopicInfo>? = null,
    var versionName: String? = null,
    var versionCode: Int? = null,
    var companyId: Int? = null,
    var active: Boolean? = false,
    var createTime: Date? = null,
    var updateTime: Date? = null
)