package com.lgguan.iot.position.bean

import io.swagger.v3.oas.annotations.media.Schema

data class AddOrUpdateDepartment(
    @field:Schema(required = true)
    val name: String,
    val parentId: Long? = 0
)
