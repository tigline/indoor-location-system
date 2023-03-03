package com.lgguan.iot.position.external

data class LocationInfo(
    val id: Int,
    val deviceId: String,
    val deviceName: String,
    val deviceType: String,
    val deviceX: String,
    val deviceY: String,
    val device1mRssi: String,
    val deviceGroup: String
)
