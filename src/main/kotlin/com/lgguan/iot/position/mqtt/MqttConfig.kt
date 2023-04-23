package com.lgguan.iot.position.mqtt

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

/**
 *
 *
 * @author N.Liu
 **/
//@Configuration
//@ConfigurationProperties(prefix = "mqtt")
class MqttConfig {
    lateinit var url: String
    lateinit var clientId: String
    var username: String? = null
    var password: String? = null
    lateinit var subscribeTopics: Array<String>
}
