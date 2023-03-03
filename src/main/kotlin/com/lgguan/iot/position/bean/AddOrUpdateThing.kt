package com.lgguan.iot.position.bean

/**
 *
 *
 * @author N.Liu
 **/
data class AddOrUpdateThing(
    val name: String,
    val typeId: Long,
    val tag: String? = null,
    val picture: String? = null
)
