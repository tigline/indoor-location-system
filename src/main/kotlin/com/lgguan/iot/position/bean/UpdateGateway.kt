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
    val setX: Double? = null,
    val setY: Double? = null,
    val setZ: Double? = null,
    val angle: Double? = null,
    var companyCode: String? = null
)
