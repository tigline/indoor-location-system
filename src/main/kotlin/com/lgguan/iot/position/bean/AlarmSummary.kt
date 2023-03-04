package com.lgguan.iot.position.bean

data class AlarmSummary(
    val total: Int,
    val statusCount: Map<AlarmStatus, Int>,
    val alarmRank: List<AlarmRank>,
    val alarmRatio: Map<AlarmType, Int>
)

data class AlarmRank(
    val rank: Int,
    val deviceId: String,
    val count: Int
)
