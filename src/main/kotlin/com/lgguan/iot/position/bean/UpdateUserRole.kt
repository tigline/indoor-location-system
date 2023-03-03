package com.lgguan.iot.position.bean

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.Pattern

/**
 *
 *
 * @author N.Liu
 **/
data class UpdateUserRole(
    @field:Schema(required = true)
    val userId: String,
    @field:Valid
    @field:Schema(required = true, allowableValues = ["Admin", "User"])
    @field:Pattern(
        regexp = "(Admin|User)",
        message = "role: Illegal role type"
    )
    val role: String
)
