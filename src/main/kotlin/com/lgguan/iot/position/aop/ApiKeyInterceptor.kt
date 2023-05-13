package com.lgguan.iot.position.aop

import cn.hutool.json.JSONUtil
import com.lgguan.iot.position.bean.IErrorCode
import com.lgguan.iot.position.bean.RestValue
import com.lgguan.iot.position.util.getApiKey
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import java.io.IOException
import java.io.PrintWriter

@Component
class ApiKeyInterceptor : HandlerInterceptor {

    private val log = LoggerFactory.getLogger(javaClass)

    @Value("\${security.api.key}")
    private val apiKey: String? = null

    @Throws(Exception::class)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val reqApiKey: String? = request.getApiKey()
        log.info("ApiKeyInterceptor apiKey:$reqApiKey")
        log.info("Value apiKey:$apiKey")
        if(!apiKey.equals(reqApiKey)){
            val body = RestValue(IErrorCode.ApiKeyErrorFailed.code, IErrorCode.ApiKeyErrorFailed.message, null)
            returnBody(response, JSONUtil.toJsonStr(body))
            return false
        }
        return super.preHandle(request, response, handler)
    }

    @Throws(Exception::class)
    private fun returnBody(response: HttpServletResponse, json: String) {
        var writer: PrintWriter? = null
        response.characterEncoding = "UTF-8"
        response.contentType = "application/json; charset=utf-8"
        try {
            writer = response.writer
            writer.print(json)
        } catch (e: IOException) {
        } finally {
            writer?.close()
        }
    }
}