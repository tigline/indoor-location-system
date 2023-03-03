package com.lgguan.iot.position.bean

import io.swagger.v3.oas.annotations.media.Schema

/**
 *
 *
 * @author N.Liu
 **/
data class AddOrUpdatePersonnelType(
    @field:Schema(required = true)
    val typeName: String,
    val picture: String? = null
)
