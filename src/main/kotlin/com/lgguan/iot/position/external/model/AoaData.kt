package com.lgguan.iot.position.external.model

import cn.hutool.core.date.DateUtil
import com.fasterxml.jackson.annotation.JsonProperty
import com.lgguan.iot.position.entity.AoaDataInfo

/**
 *
 *
 * @author N.Liu
 **/
data class AoaData(
    @JsonProperty("Gateway")
    val gateway: String,
    @JsonProperty("NodeId")
    val nodeId: String,
    @JsonProperty("SystemId")
    val systemId: String? = null,
    @JsonProperty("Type")
    val type: String,
    @JsonProperty("mac")
    val mac: String? = null,
    @JsonProperty("motion")
    val motion: String? = null,
    @JsonProperty("opt_scale")
    val optScale: Float? = null,
    @JsonProperty("position_type")
    val positionType: String? = null,
    @JsonProperty("x")
    val posX: Float? = null,
    @JsonProperty("y")
    val posY: Float? = null
)

fun AoaData.toInfo(): AoaDataInfo {
    val info = AoaDataInfo()
    info.deviceId = nodeId
    info.optScale = optScale
    info.posX = posX
    info.posY = posY
    info.status = if (motion == "freezing") 0 else 1
    info.timestamp = DateUtil.currentSeconds()
    return info
}
