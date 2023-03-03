package com.lgguan.iot.position.ws

import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor

/**
 *
 *
 * @author N.Liu
 **/
class WebSocketInterceptor: HttpSessionHandshakeInterceptor() {
    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Boolean {
        if (request is ServletServerHttpRequest) {
            val userId = request.servletRequest.getParameter("userId")
            if (userId.isNullOrEmpty()) {
                return false
            }
            attributes["userId"] = userId
            return super.beforeHandshake(request, response, wsHandler, attributes)
        }
        return true
    }
}
