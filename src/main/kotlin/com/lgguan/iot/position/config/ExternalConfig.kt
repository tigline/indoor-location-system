package com.lgguan.iot.position.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "external")
class ExternalConfig {
    lateinit var url: String
}
