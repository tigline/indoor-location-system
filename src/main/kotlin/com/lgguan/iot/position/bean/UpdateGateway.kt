package com.lgguan.iot.position.bean

import io.swagger.v3.oas.annotations.media.Schema

/**
 *
 *
 * @author N.Liu
 **/
data class UpdateGateway(
    val name: String? = null,
    @field:Schema(required = true)
    val mapId: String,
    val setX: Float? = null,
    val setY: Float? = null,
    val setZ: Float? = null,
    val angle: Float? = null,
    var companyCode: String? = null
)
