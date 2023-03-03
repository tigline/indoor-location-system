package com.lgguan.iot.position.bean

import io.swagger.v3.oas.annotations.media.Schema

/**
 *
 *
 * @author N.Liu
 **/
data class UpdateGateway(
    @field:Schema(required = true)
    val name: String,
    @field:Schema(required = true)
    val mapId: String,
    val setX: Float? = null,
    val setY: Float? = null,
    val setZ: Float? = null,
    val angle: Float? = null
)
