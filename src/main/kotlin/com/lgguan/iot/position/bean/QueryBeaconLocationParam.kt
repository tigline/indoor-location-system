package com.lgguan.iot.position.bean

import io.swagger.v3.oas.annotations.media.Schema

data class QueryBeaconLocationParam(
    @field:Schema(required = true)
    val mapId: String,
    @field:Schema(required = true)
    val startTime: Long,
    @field:Schema(required = true)
    val endTime: Long,
    val deviceId: String? = null
)
