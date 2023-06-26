package com.lgguan.iot.position

import cn.hutool.core.date.DateUtil
import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@SpringBootApplication
@MapperScan("com.lgguan.iot.position.mapper")
@EnableScheduling
class Application{
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}

var systemRunningTime = DateUtil.currentSeconds()

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
