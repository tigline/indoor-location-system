package com.lgguan.iot.position.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.io.File

@Configuration
class UploadConfig: WebMvcConfigurer {

    /**
     * 上传文件存储在本地的根路径
     */
    @Value("\${indoor.file.path}")
    val localFilePath: String? = null

    /**
     * 资源映射路径 前缀
     */
    @Value("\${indoor.file.prefix}")
    var localFilePrefix: String? = null

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        /** 本地文件上传路径  */
        registry.addResourceHandler("$localFilePrefix/**")
            .addResourceLocations("file:" + localFilePath + File.separator)
    }

    /**
     * 开启跨域
     */
    override fun addCorsMappings(registry: CorsRegistry) {
        // 设置允许跨域的路由
        registry.addMapping("$localFilePrefix/**")
            .allowedOrigins("*")
            .allowedMethods("GET")
    }
}