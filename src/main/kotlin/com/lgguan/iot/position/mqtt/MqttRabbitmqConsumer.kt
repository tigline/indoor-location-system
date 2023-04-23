package com.lgguan.iot.position.mqtt

import com.lgguan.iot.position.external.ExternalMqttMessageHandler
import com.rabbitmq.client.Channel
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.support.AmqpHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class MqttRabbitmqConsumer(val messageHandler: ExternalMqttMessageHandler) {

    private val log = LoggerFactory.getLogger(javaClass)

    @RabbitHandler
    @RabbitListener(queues = arrayOf("device.data.queue"), concurrency = "10-100")
    fun mqttDataQueue(channel: Channel,
                      @Header(AmqpHeaders.DELIVERY_TAG) deliveryTag: Long,
                      @Header("topic") topic: String,
                      @Payload payload: String) {
        try {
            log.info("rabbitmq consumer data payload:" + payload)
            log.info("rabbitmq consumer data topic:" + topic)
            messageHandler.handler(topic, payload)
        } catch (e:Exception) {
            log.error("mqttDataQueue error:" + e.message);
        }finally{
            channel.basicAck(deliveryTag, false);
        }
    }
}