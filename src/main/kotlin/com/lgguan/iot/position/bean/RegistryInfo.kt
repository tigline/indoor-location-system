package com.lgguan.iot.position.bean

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class RegistryInfo(
    @field:Schema(required = true)
    @field:Pattern(
        regexp = "^[a-zA-Z0-9_!#\$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\$",
        message = "username: Illegal email address"
    )
    val username: String,
    @field:Schema(required = true)
    @field:NotBlank(message = "password: Can not be empty")
    val password: String,
    val nickname: String? = null,
    @field:Pattern(
        regexp = "^[a-zA-Z0-9_!#\$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\$",
        message = "email: Illegal email address"
    )
    val email: String? = null,
    val phone: String? = null
)

