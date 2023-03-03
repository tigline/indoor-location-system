package com.lgguan.iot.position.external

import com.fasterxml.jackson.annotation.JsonProperty

data class LocationInfoRes(
    @JsonProperty("Id")
    val id: Int,
    @JsonProperty("DeviceId")
    val deviceId: String,
    @JsonProperty("DeviceName")
    val deviceName: String,
    @JsonProperty("DeviceType")
    val deviceType: String,
    @JsonProperty("DeviceX")
    val deviceX: String,
    @JsonProperty("DeviceY")
    val deviceY: String,
    @JsonProperty("Device1mRssi")
    val device1mRssi: String,
    @JsonProperty("DeviceGroup")
    val deviceGroup: String
)

fun LocationInfoRes.toLocationInfo() =
    LocationInfo(id, deviceId, deviceName, deviceType, deviceX, deviceY, device1mRssi, deviceGroup)
