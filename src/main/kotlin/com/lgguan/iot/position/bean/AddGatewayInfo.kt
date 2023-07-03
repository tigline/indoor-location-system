package com.lgguan.iot.position.bean

import io.swagger.v3.oas.annotations.media.Schema

/**
 *
 *
 * @author N.Liu
 **/
data class AddGatewayInfo(
    @field:Schema(required = true)
    val gateway: String,
    val name: String? = null,
    val mapId: String? = null,
    val groupId: String? = null,
    val productName: String? = null,
    val type: GatewayType? = GatewayType.Gateway,
    val setX: Double? = null,
    val setY: Double? = null,
    val setZ: Double? = null,
    val angle: Double? = null,
    var companyCode: String? = null
)
