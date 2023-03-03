package com.lgguan.iot.position.bean

/**
 * Token claim map
 *
 * @author N.Liu
 **/
class ClaimMap(userId: String = "", role: String = "") {
    private val map = mutableMapOf(
        "userId" to userId,
        "role" to role
    )
    val keys: Set<String>
        get() = map.keys

    operator fun set(key: String, value: String) {
        map[key] = value
    }

    operator fun get(key: String): String? {
        return map[key]
    }

    fun forEach(action: (Map.Entry<String, String>) -> Unit) {
        for (element in map) action(element)
    }
}
