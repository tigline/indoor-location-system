package com.lgguan.iot.position.bean


data class IotAuthorityResponse(
    val clientId: String,
    val companyCode: String,
    val groupId: String,
    val modelCode: String,
    val resultCode: Int,
    val status: String,
    val message: String
)