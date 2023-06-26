package com.lgguan.iot.position.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.concurrent.Executors
import com.lgguan.iot.position.entity.BeaconInfo
import com.lgguan.iot.position.entity.AoaDataInfo

@Service
class TaskQueueService(
    private val redisTemplate: StringRedisTemplate, // 需要将StringRedisTemplate注入到TaskQueueService中
    private val objectMapper: ObjectMapper, // 需要将ObjectMapper注入到TaskQueueService中
    //private val gatewayAndBeaconService: GatewayAndBeaconService // 需要将GatewayAndBeaconService注入到TaskQueueService中
) {

    private lateinit var gatewayAndBeaconService: GatewayAndBeaconService

    fun setGatewayAndBeaconService(service: GatewayAndBeaconService) {
        this.gatewayAndBeaconService = service
    }
    companion object {
        private const val TASK_QUEUE_KEY = "taskQueue"
    }

    private val executor = Executors.newFixedThreadPool(20)

    private val listOperations = redisTemplate.opsForList()

    // 添加任务到队列
    fun addTaskToQueue(taskType: String, taskData: Any) {
        val task = Task(taskType, objectMapper.writeValueAsString(taskData))
        val taskJson = objectMapper.writeValueAsString(task)
        listOperations.rightPush(TASK_QUEUE_KEY, taskJson)
    }

    // 处理任务队列
    @Scheduled(fixedRate = 500) // 每500ms执行一次
    fun processTaskQueue() {
        val taskJson = listOperations.leftPop(TASK_QUEUE_KEY)
        if (taskJson != null) {
            executor.execute {
                val task = objectMapper.readValue(taskJson, Task::class.java)
                // 根据任务的类型来处理任务
                when (task.type) {
                    "AoaDataInfo" -> {
                        val aoaDataInfo = objectMapper.readValue(task.data, AoaDataInfo::class.java)
                        // 在这里处理你的AoaDataInfo任务，例如更新或插入到数据库
                        gatewayAndBeaconService.insertAoaDataInfo(aoaDataInfo)
                    }
                    "BeaconInfo" -> {
                        val beaconInfo = objectMapper.readValue(task.data, BeaconInfo::class.java)
                        // 在这里处理你的BeaconInfo任务，例如更新或插入到数据库
                        gatewayAndBeaconService.updateBeaconInfo(beaconInfo)
                    }
                }
            }
        }
    }
}

// 通用的任务类
data class Task(
    val type: String,
    val data: String
)
