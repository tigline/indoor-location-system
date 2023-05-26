package com.lgguan.iot.position.util.file

import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.net.URL
import java.util.*

/**
 * 图片处理工具类
 *
 */
object ImageUtils {
    private val log = LoggerFactory.getLogger(ImageUtils::class.java)
    fun getImage(imagePath: String?): ByteArray? {
        val `is` = getFile(imagePath)
        return try {
            IOUtils.toByteArray(`is`)
        } catch (e: Exception) {
            log.error("图片加载异常 {}", e)
            null
        } finally {
            IOUtils.closeQuietly(`is`)
        }
    }

    fun getFile(imagePath: String?): InputStream? {
        try {
            var result = readFile(imagePath)
            result = Arrays.copyOf(result, result!!.size)
            return ByteArrayInputStream(result)
        } catch (e: Exception) {
            log.error("获取图片异常 {}", e)
        }
        return null
    }

    /**
     * 读取文件为字节数据
     *
     * @param url 地址
     * @return 字节数据
     */
    fun readFile(url: String?): ByteArray? {
        var `in`: InputStream? = null
        return try {
            // 网络地址
            val urlObj = URL(url)
            val urlConnection = urlObj.openConnection()
            urlConnection.connectTimeout = 30 * 1000
            urlConnection.readTimeout = 60 * 1000
            urlConnection.doInput = true
            `in` = urlConnection.getInputStream()
            IOUtils.toByteArray(`in`)
        } catch (e: Exception) {
            log.error("访问文件异常 {}", e)
            null
        } finally {
            IOUtils.closeQuietly(`in`)
        }
    }
}