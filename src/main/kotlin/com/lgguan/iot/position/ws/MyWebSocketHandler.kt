package com.lgguan.iot.position.ws

import com.lgguan.iot.position.bean.WsMessage
import com.lgguan.iot.position.util.toJsonStr
import org.apache.commons.collections.CollectionUtils
import org.slf4j.LoggerFactory
import org.springframework.web.socket.*
import java.util.*


/**
 *
 *
 * @author N.Liu
 **/
private val log = LoggerFactory.getLogger(MyWebSocketHandler::class.java)

class MyWebSocketHandler : WebSocketHandler {

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val userId = session.attributes["userId"] as String
        if(sessionMap.containsKey(userId)){
            var userSessionSet = sessionMap[userId]
            userSessionSet!!.add(session)
            sessionMap[userId] = userSessionSet
        } else {
            var userSessionSet = hashSetOf(session)
            sessionMap[userId] = userSessionSet
        }
        log.info("WebSocket connected by: [$userId]")
    }

    override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
        val userId = session.attributes["userId"]
        log.info("WebSocket message by: [$userId]")
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        val userId = session.attributes["userId"] as String
        log.error("WebSocket error by: [$userId], exception: [${exception.message}]")
        removeWebSession(session)
    }

    override fun afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {
        val userId = session.attributes["userId"] as String
        log.warn("WebSocket disconnect closed by: [$userId], " +
                "closeStatus.code: [${closeStatus.code}], " +
                "closeStatus.reason: [${closeStatus.reason}]")
        removeWebSession(session)
    }

    override fun supportsPartialMessages(): Boolean {
        return false
    }

    private fun removeWebSession(session: WebSocketSession){
        val userId = session.attributes["userId"] as String
        if(sessionMap.containsKey(userId)){
            var userSessionSet = sessionMap[userId]
            if(CollectionUtils.isNotEmpty(userSessionSet)){
                val oldSession = userSessionSet!!.stream().filter { session.id == it.id }.findFirst().get()
                if(Objects.nonNull(oldSession)){
                    userSessionSet.remove(oldSession)
                    if(CollectionUtils.isNotEmpty(userSessionSet)){
                        sessionMap[userId] = userSessionSet
                    } else {
                        sessionMap.remove(userId)
                    }
                }
            }
        }
    }
}

val sessionMap by lazy {
    mutableMapOf<String, HashSet<WebSocketSession>>()
}

fun <T> sendWsMessage(wsMessage: WsMessage<T>) {
    val message = wsMessage.toJsonStr()
    sessionMap.values.forEach {sessionSet: HashSet<WebSocketSession> ->
        sessionSet.stream().forEach { session ->
            if (session.isOpen) {
                try {
                    session.sendMessage(TextMessage(message))
                } catch (e: Exception) {
                    log.error("Send websocket message error: ${e.message}")
                }
            } else {
                log.warn("web session is failure: [${session.attributes}]")
            }
        }
    }
}
