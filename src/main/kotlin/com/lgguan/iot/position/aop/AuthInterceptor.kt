package com.lgguan.iot.position.aop

import com.lgguan.iot.position.bean.IErrorCode
import com.lgguan.iot.position.bean.failedOf
import com.lgguan.iot.position.util.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

/**
 *
 *
 * @author N.Liu
 **/
@Component
class AuthInterceptor : HandlerInterceptor {
    @Throws(Exception::class)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        var login = false
        val token: String? = request.getToken()
        if (!token.isNullOrEmpty()) {
            if (token.verify(TokenType.Access) && tokenCache.getIfPresent(token) != null) {
                login = true
            }
        }
        if (!login) {
            response.status = 401
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            response.writer.append(failedOf<Void>(IErrorCode.Unauthorized).toString())
        }
        return login
    }
}
