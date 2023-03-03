package com.lgguan.iot.position.external

import com.lgguan.iot.position.config.ExternalConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

@Configuration
class RetrofitConfigure(externalConfig: ExternalConfig) {
    private val retrofit = Retrofit.Builder()
        .baseUrl(externalConfig.url)
        .addConverterFactory(JacksonConverterFactory.create())
        .build()

    @Bean
    fun acServerApi(): AcServerApi {
        return retrofit.create(AcServerApi::class.java)
    }
}
