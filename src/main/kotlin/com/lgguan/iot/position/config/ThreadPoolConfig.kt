package com.lgguan.iot.position.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.CustomizableThreadFactory
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

@Configuration
class ThreadPoolConfig {

    private val CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() + 1
    private val MAX_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2
    private val QUEUE_CAPACITY = 20
    private val KEEP_ALIVE_SECONDS = 120

    @Bean(name = ["iotTaskExecutor"], destroyMethod = "shutdown")
    fun taskExecutor(): ThreadPoolTaskExecutor {
        val threadPoolTaskExecutor = ThreadPoolTaskExecutor()
        threadPoolTaskExecutor.corePoolSize = CORE_POOL_SIZE
        threadPoolTaskExecutor.maxPoolSize = MAX_POOL_SIZE
        threadPoolTaskExecutor.queueCapacity = QUEUE_CAPACITY
        threadPoolTaskExecutor.keepAliveSeconds = KEEP_ALIVE_SECONDS
        threadPoolTaskExecutor.setThreadNamePrefix("iot-task-")
        threadPoolTaskExecutor.setRejectedExecutionHandler(ThreadPoolExecutor.CallerRunsPolicy())
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true)
        return threadPoolTaskExecutor
    }

    @Bean(name = ["wsTaskExecutor"], destroyMethod = "shutdown")
    fun wsTaskExecutor(): ThreadPoolExecutor {
        val threadFactory: ThreadFactory = CustomizableThreadFactory("ws-task-")
        val poolSize: Int = CORE_POOL_SIZE * 10
        return ThreadPoolExecutor(
            poolSize,
            poolSize * 2,
            60,
            TimeUnit.SECONDS,
            LinkedBlockingDeque(),
            threadFactory
        )
    }
}