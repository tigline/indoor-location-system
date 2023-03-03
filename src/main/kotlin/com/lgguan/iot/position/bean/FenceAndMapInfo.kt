package com.lgguan.iot.position.bean

import com.lgguan.iot.position.util.Point

/**
 *
 *
 * @author N.Liu
 **/
data class FenceAndMapInfo(
    val fenceId: String,
    val name: String,
    val mapId: String,
    val mapName: String? = null,
    val type: FenceType,
    val points: List<Point>,
    val enabled: Boolean
)
