package com.lgguan.iot.position.bean

import io.swagger.v3.oas.annotations.media.Schema

/**
 *
 *
 * @author N.Liu
 **/
data class PageLimit(
    @field:Schema(description = "当前页码", defaultValue = "1")
    val current: Long = 1,
    @field:Schema(description = "每页数量", defaultValue = "10")
    val size: Long = 10
)
