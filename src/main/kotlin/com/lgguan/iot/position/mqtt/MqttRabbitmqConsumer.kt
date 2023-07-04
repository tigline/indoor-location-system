package com.lgguan.iot.position.mqtt

import com.lgguan.iot.position.external.ExternalAllMessageHandler
import com.rabbitmq.client.Channel
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.support.AmqpHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class MqttRabbitmqConsumer(val messageHandler: ExternalAllMessageHandler) {

    private val log = LoggerFactory.getLogger(javaClass)

    @RabbitHandler
    @RabbitListener(queues = ["device.data.queue"], concurrency = "10-100")
    fun mqttDataQueue(channel: Channel,
                      @Header(AmqpHeaders.DELIVERY_TAG) deliveryTag: Long,
                      @Header("topic") topic: String,
                      @Header("companyCode") companyCode: String,
                      @Header("modelCode") modelCode: String,
                      @Header("type") type: String,
                      @Header("sn") sn: String,
                      @Payload payload: String) {
        try {
            channel.basicAck(deliveryTag, true)
            log.info("rabbitmq consumer data payload:$payload")
            log.info("rabbitmq consumer data companyCode:$companyCode")
            log.info("rabbitmq consumer data modelCode:$modelCode")
            log.info("rabbitmq consumer data type:$type")
            log.info("rabbitmq consumer data sn:$sn")
            log.info("rabbitmq consumer data topic:$topic")
            messageHandler.handler(payload, topic, companyCode, type)
        } catch (e:Exception) {
            log.error("mqttDataQueue error:" + e.message)
        }
//        finally{
//            //channel.basicAck(deliveryTag, false);
//        }
    }
}