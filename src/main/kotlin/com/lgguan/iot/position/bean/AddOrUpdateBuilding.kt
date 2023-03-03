package com.lgguan.iot.position.bean

import io.swagger.v3.oas.annotations.media.Schema

/**
 *
 *
 * @author N.Liu
 **/
data class AddOrUpdateBuilding(
    @field:Schema(required = true)
    val name: String,
    val address: String? = null,
    val picture: String? = null
)
