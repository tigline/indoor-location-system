package com.lgguan.iot.position.bean

data class WsMessage<T>(
    val type: MessageType,
    val data: T
)

enum class MessageType {
    AOAData,
    Alarm,
    Fence,
    OnlineStatus,
}
