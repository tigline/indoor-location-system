package com.lgguan.iot.position.config

import com.lgguan.iot.position.aop.AuthInterceptor
import com.lgguan.iot.position.aop.RequestLogInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 *
 *
 * @author N.Liu
 **/
@Configuration
class WebMvcConfig(private val authInterceptor: AuthInterceptor, private val requestLogInterceptor: RequestLogInterceptor): WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(requestLogInterceptor)
        registry.addInterceptor(authInterceptor)
            .excludePathPatterns("/api/v1/register", "/api/v1/login", "/api/v1/refreshToken", "/test/**",
                "/v3/api-docs/**", "/doc.html", "/swagger-ui/**", "/webjars/**", "/favicon.ico")
    }
}
