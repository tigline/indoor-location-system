package com.lgguan.iot.position.bean

import io.swagger.v3.oas.annotations.media.Schema

/**
 *
 *
 * @author N.Liu
 **/
data class AddBeaconInfo(
    @field:Schema(required = true)
    val deviceId: String,
    @field:Schema(required = true)
    val type: BeaconType,
    val name: String? = null,
    val gateway: String? = null,
    val mac: String? = null,
    val mapId: String? = null,
    val groupId: String? = null,
    val zoneId: String? = null,
    val productName: String? = null,
    var companyCode: String? = null
)
