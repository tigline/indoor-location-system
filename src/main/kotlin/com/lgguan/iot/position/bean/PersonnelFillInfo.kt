package com.lgguan.iot.position.bean

/**
 *
 *
 * @author N.Liu
 **/
data class PersonnelFillInfo(
    val personnelId: Long,
    val name: String,
    val sex: SexType,
    val tag: String? = null,
    val typeId: Long? = null,
    val typeName: String? = null,
    val depId: Long? = null,
    val depName: String? = null,
    val avatar: String? = null
)
