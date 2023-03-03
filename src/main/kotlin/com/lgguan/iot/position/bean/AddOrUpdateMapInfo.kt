package com.lgguan.iot.position.bean

import io.swagger.v3.oas.annotations.media.Schema

data class AddOrUpdateMapInfo(
    @field:Schema(required = true)
    val name: String,
    @field:Schema(required = true)
    val buildingId: String,
    @field:Schema(required = true)
    val floor: String,
    @field:Schema(required = true)
    val picture: String,
    val width: Float? = null,
    val length: Float? = null,
    val widthPx: Float? = null,
    val lengthPx: Float? = null
)
