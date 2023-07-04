package com.lgguan.iot.position.external.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 *
 *
 * @author N.Liu
 **/
data class AoaStationHis(
    @JsonProperty("Gateway")
    val gateway: String,
    @JsonProperty("SystemId")
    val systemId: String,
    @JsonProperty("Type")
    val type: String,
    @JsonProperty("his_x")
    val hisX: Double,
    @JsonProperty("his_y")
    val hisY: Double,
    @JsonProperty("his_z")
    val hisZ: Double
)
