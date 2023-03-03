package com.lgguan.iot.position.mqtt

import org.springframework.integration.annotation.MessagingGateway
import org.springframework.integration.mqtt.support.MqttHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Component

/**
 *
 *
 * @author N.Liu
 **/
@Component
@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
interface IMqttSender {
    fun sendToMqtt(@Header(MqttHeaders.TOPIC) topic: String, payload: String)
}
