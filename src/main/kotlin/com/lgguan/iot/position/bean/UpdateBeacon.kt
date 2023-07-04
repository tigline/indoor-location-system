package com.lgguan.iot.position.bean

import io.swagger.v3.oas.annotations.media.Schema

data class UpdateBeacon(

    @field:Schema(required = true)
    val type: BeaconType,
    @field:Schema(required = true)
    var companyCode: String,
    val fenceIds: String? = null,
    val name: String? = null,
)
