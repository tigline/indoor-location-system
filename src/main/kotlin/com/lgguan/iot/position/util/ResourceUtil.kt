package com.lgguan.iot.position.util

import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory

class ResourceUtil {

    private val log = LoggerFactory.getLogger(javaClass)

    val UTF8 = "utf-8"

    fun readResourceJson(path: String?): String? {
        var res: String? = null
        val `is` = Thread.currentThread().contextClassLoader.getResourceAsStream(path)
        try {
            res = IOUtils.toString(`is`, UTF8)
            `is`.close()
        } catch (e: Exception) {
            log.error("Unable to load resource: {}", path)
        }
        return res
    }

}