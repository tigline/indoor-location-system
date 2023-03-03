package com.lgguan.iot.position.bean

import com.lgguan.iot.position.util.Point
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.Size

/**
 *
 *
 * @author N.Liu
 **/
data class AddOrUpdateFenceInfo(
    @field:Schema(required = true)
    val name: String,
    @field:Schema(required = true)
    val mapId: String,
    @field:Schema(required = true)
    @field:Size(min = 3, message = "points: Items must be greater than 2")
    @field:Valid
    val points: List<Point>,
    @field:Schema(required = true)
    val type: FenceType
)
