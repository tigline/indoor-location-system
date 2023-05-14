package com.lgguan.iot.position.external

import cn.hutool.json.JSONUtil
import com.lgguan.iot.position.config.RabbitmqConfig
import com.lgguan.iot.position.entity.Model
import com.lgguan.iot.position.service.ModelService
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class ModelEventHandler(val modelService: ModelService,
                        val rabbitTemplate: RabbitTemplate,
                        val rabbitmqConfig: RabbitmqConfig) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Async
    @EventListener
    fun modelUpdateEventHandle(model: Model) {
        log.info("event model:" + JSONUtil.toJsonStr(model))
        val modelData = model.id?.let { modelService.getModelInfoByModelId(it) }
        log.info("modelData:" + JSONUtil.toJsonStr(modelData))
        if (modelData != null) {
            log.info("rabbitmq data:" + JSONUtil.toJsonStr(modelData))
            rabbitTemplate.convertAndSend(rabbitmqConfig.fanoutExchangeName, "", modelData)
        }
    }
}