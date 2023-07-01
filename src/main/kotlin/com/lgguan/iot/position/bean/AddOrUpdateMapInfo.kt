package com.lgguan.iot.position.bean

import com.lgguan.iot.position.entity.CoordinateType
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
    @field:Schema(required = true)
    val companyCode: String,
    @field:Schema(required = true)
    val coordinateType: CoordinateType,
    val width: Float? = null,
    val length: Float? = null,
    val widthPx: Float? = null,
    val lengthPx: Float? = null
)
