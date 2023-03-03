package com.lgguan.iot.position.bean

/**
 *
 *
 * @author N.Liu
 **/
data class PageResult<T>(
    val current: Long,
    val size: Long,
    val total: Long,
    val items: List<T>
)
