package com.lgguan.iot.position.config

import lombok.Getter
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory

@Getter
@Configuration
class RabbitmqConfig {

    @Value("\${spring.rabbitmq.exchange.fanout-direct-exchange}")
    val fanoutExchangeName: String = ""

    @Value("\${spring.rabbitmq.exchange.fanout-direct-exchange-command}")
    val commandFanoutExchangeName: String = ""

    @Value("\${spring.rabbitmq.queueName.fanout-model-queue}")
    val fanoutQueueName: String = ""

    @Value("\${spring.rabbitmq.queueName.fanout-command-queue}")
    val commandFanoutQueueName: String = ""

    @Bean
    fun fanoutExchange(): FanoutExchange {
        return FanoutExchange(fanoutExchangeName)
    }

    @Bean
    fun commandFanoutExchange(): FanoutExchange {
        return FanoutExchange(commandFanoutExchangeName)
    }

    @Bean
    fun fanoutQueue(): Queue {
        return Queue(fanoutQueueName, true)
    }

    @Bean
    fun commandFanoutQueue(): Queue {
        return Queue(commandFanoutQueueName, true)
    }

    @Bean
    fun bindingFanoutQueue(): Binding {
        return BindingBuilder.bind(fanoutQueue()).to(fanoutExchange())
    }

    @Bean
    fun bindingCommandFanoutQueue(): Binding {
        return BindingBuilder.bind(commandFanoutQueue()).to(commandFanoutExchange())
    }

    @Bean
    fun messageHandlerMethodFactory(): MessageHandlerMethodFactory {
        val messageHandlerMethodFactory = DefaultMessageHandlerMethodFactory()
        messageHandlerMethodFactory.setMessageConverter(mappingJackson2MessageConverter())
        return messageHandlerMethodFactory
    }

    @Bean
    fun mappingJackson2MessageConverter(): MappingJackson2MessageConverter {
        return MappingJackson2MessageConverter()
    }

    // 提供自定义RabbitTemplate,将对象序列化为json串
    @Bean
    fun jacksonRabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate? {
        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.messageConverter = Jackson2JsonMessageConverter()
        return rabbitTemplate
    }


}