package com.lgguan.iot.position.config

import com.lgguan.iot.position.bean.IErrorCode
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.parameters.HeaderParameter
import io.swagger.v3.oas.models.responses.ApiResponse
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


/**
 *
 *
 * @author N.Liu
 **/
@Configuration
class SwaggerConfig {

    @Bean
    fun globalGroupedApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("ips")
            .displayName("室内定位系统")
            .addOperationCustomizer { operation, _ ->
                val responses = operation.responses
                IErrorCode.values().filter { it.code != 200 }.forEach {
                    responses.addApiResponse(it.code.toString(), ApiResponse().description(it.message))
                }
                operation.addParametersItem(
                    HeaderParameter()
                        .name("Authorization")
                        .required(true)
                        .description("Bearer accessToken")
                ).responses(responses)
            }
            .packagesToScan("com.lgguan.iot.position")
            .build()
    }

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("室内定位系统")
                    .version("1.0")
                    .description("室内定位系统后端接口文档")
                    .contact(Contact().name("lgguan").email("lgguan@live.cn"))
            )
    }
}
