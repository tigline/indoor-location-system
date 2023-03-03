package com.lgguan.iot.position.bean

import io.swagger.v3.oas.annotations.media.Schema

/**
 *
 *
 * @author N.Liu
 **/
data class AddOrUpdatePersonnel(
    @field:Schema(required = true)
    val name: String,
    @field:Schema(required = true)
    val sex: SexType,
    val tag: String? = null,
    @field:Schema(required = true)
    val typeId: Long,
    val depId: Long? = null,
    val avatar: String? = null
)
