package com.lgguan.iot.position.bean

import io.swagger.v3.oas.annotations.media.Schema

data class UpdateBeacon(
    @field:Schema(required = true)
    val name: String,
    @field:Schema(required = true)
    val type: BeaconType,
    val fenceIds: String? = null
)
