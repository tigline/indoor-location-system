package com.lgguan.iot.position.ws

import com.lgguan.iot.position.bean.WsMessage
import com.lgguan.iot.position.util.toJsonStr
import org.slf4j.LoggerFactory
import org.springframework.web.socket.*


/**
 *
 *
 * @author N.Liu
 **/
private val log = LoggerFactory.getLogger(MyWebSocketHandler::class.java)

class MyWebSocketHandler : WebSocketHandler {
    override fun afterConnectionEstablished(session: WebSocketSession) {
//        val userId = session.attributes["userId"] as String
//        sessionMap[userId] = session
//        log.info("WebSocket connected by: [$userId]")

        val userId = session.attributes["userId"] as String
        sessionMap.getOrPut(userId) { mutableListOf() }.add(session)
        log.info("WebSocket connected by: [$userId]")
    }

    override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
        val userId = session.attributes["userId"]
        log.info("WebSocket message by: [$userId]")
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        val userId = session.attributes["userId"] as String
        sessionMap.remove(userId)
        log.info("WebSocket error by: [$userId]")
    }

    override fun afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {
//        val userId = session.attributes["userId"] as String
//        sessionMap.remove(userId)
//        log.info("WebSocket disconnected by: [$userId]")

        val userId = session.attributes["userId"] as String
        sessionMap[userId]?.remove(session)
        log.info("WebSocket disconnected by: [$userId]")
    }

    override fun supportsPartialMessages(): Boolean {
        return false
    }
}

val sessionMap by lazy {
    //mutableMapOf<String, WebSocketSession>()
    mutableMapOf<String, MutableList<WebSocketSession>>()
}

fun <T> sendWsMessage(wsMessage: WsMessage<T>) {
    val message = wsMessage.toJsonStr()
//    sessionMap.values.forEach { session ->
//        if (session.isOpen) {
//            try {
//                session.sendMessage(TextMessage(message))
//            } catch (e: Exception) {
//                log.error("Send websocket message error: ${e.message}")
//            }
//        }
//    }
    sessionMap.values.flatten().forEach { session ->
        if (session.isOpen) {
            try {
                session.sendMessage(TextMessage(message))
            } catch (e: Exception) {
                log.error("Send websocket message error: ${e.message}")
            }
        }
    }
}
