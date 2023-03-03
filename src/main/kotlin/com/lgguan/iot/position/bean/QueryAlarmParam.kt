package com.lgguan.iot.position.bean

data class QueryAlarmParam(
    val deviceId: String? = null,
    val fenceId: String? = null,
    val type: AlarmType? = null,
    val status: AlarmStatus? = null,
    val startTime: Long? = null,
    val endTime: Long? = null
)
