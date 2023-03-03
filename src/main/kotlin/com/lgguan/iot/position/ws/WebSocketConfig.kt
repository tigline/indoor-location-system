package com.lgguan.iot.position.ws

import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

/**
 *
 *
 * @author N.Liu
 **/
@Configuration
@EnableWebSocket
class WebSocketConfig: WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(MyWebSocketHandler(), "/websocket")
            .setAllowedOrigins("*")
            .addInterceptors(WebSocketInterceptor())
    }
}
