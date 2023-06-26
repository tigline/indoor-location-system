package com.lgguan.iot.position

import cn.hutool.core.date.DateUtil
import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.transaction.annotation.EnableTransactionManagement

@EnableTransactionManagement
@SpringBootApplication
@MapperScan("com.lgguan.iot.position.mapper")
class Application{
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}

var systemRunningTime = DateUtil.currentSeconds()

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
