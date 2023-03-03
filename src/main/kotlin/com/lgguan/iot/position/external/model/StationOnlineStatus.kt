package com.lgguan.iot.position.external.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.lgguan.iot.position.bean.OnlineStatus

/**
 *
 *
 * @author N.Liu
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
data class StationOnlineStatus(
    @JsonProperty("SystemId")
    val systemId: String,
    @JsonProperty("Type")
    val type: String,
    @JsonProperty("online_status")
    val onlineStatus: List<OnlineStatusObj>
)

data class OnlineStatusObj(
    @JsonProperty("gateway")
    val gateway: String,
    @JsonProperty("status")
    val status: OnlineStatus,
    @JsonProperty("ip")
    val ip: String
)
