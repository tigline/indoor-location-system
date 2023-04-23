package com.lgguan.iot.position.mqtt

import com.lgguan.iot.position.external.ExternalMqttMessageHandler
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.channel.DirectChannel
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.integration.mqtt.inbound.Mqttv5PahoMessageDrivenChannelAdapter
import org.springframework.integration.mqtt.outbound.Mqttv5PahoMessageHandler
import org.springframework.integration.mqtt.support.MqttHeaders
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel


//@Configuration
class MqttV5Configuration(val mqttConfig: MqttConfig, val messageHandler: ExternalMqttMessageHandler) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun mqttConnectionOptions(): MqttConnectionOptions {
        return MqttConnectionOptions().apply {
            serverURIs = arrayOf(mqttConfig.url)
            userName = mqttConfig.username
            password = mqttConfig.password?.toByteArray()
            isCleanStart = true
            keepAliveInterval = 120
        }
    }

    @Bean
    fun mqttOutbound(): Mqttv5PahoMessageHandler {
        return Mqttv5PahoMessageHandler(mqttConnectionOptions(), mqttConfig.clientId).apply {
            setAsync(true)
            setAsyncEvents(true)
            setCompletionTimeout(5000)
        }
    }

    @Bean
    fun mqttOutboundChannel(): MessageChannel {
        return DirectChannel()
    }

    @Bean
    fun mqttOutFlow(): IntegrationFlow {
        return IntegrationFlow.from(mqttOutboundChannel())
            .handle(mqttOutbound())
            .get()
    }

    @Bean
    fun mqttInbound(): Mqttv5PahoMessageDrivenChannelAdapter {
        val adapter = Mqttv5PahoMessageDrivenChannelAdapter(
            mqttConnectionOptions(),
            "${mqttConfig.clientId}-in",
            *mqttConfig.subscribeTopics
        )
        adapter.setCompletionTimeout(5000)
        adapter.setQos(1)
        return adapter
    }

    @Bean
    fun mqttInputChannel(): MessageChannel {
        return DirectChannel()
    }

    @Bean
    fun mqttInFlow(): IntegrationFlow {
        return IntegrationFlow.from(mqttInbound())
            .channel(mqttInputChannel())
            .get()
    }

    @ServiceActivator(inputChannel = "mqttInputChannel")
    fun handleMessage(message: Message<String>) {
        val topic = message.headers[MqttHeaders.RECEIVED_TOPIC].toString()
        val payload = message.payload
        try {
            messageHandler.handler(topic, payload)
        } catch (e: Exception) {
            log.error("Handler mqtt message error: ${e.message}, the topic [$topic] and payload [$payload]")
        }
    }

}
