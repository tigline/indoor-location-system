package com.lgguan.iot.position.exception

import com.lgguan.iot.position.bean.IErrorCode
import com.lgguan.iot.position.bean.RestValue
import com.lgguan.iot.position.bean.failedOf
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 *
 *
 * @author N.Liu
 **/
@RestControllerAdvice
class GlobalExceptionHandler {
    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(TokenGenerateException::class)
    fun accessTokenGenerateException(e: TokenGenerateException) = failedOf<Void>(IErrorCode.GeneAccessTokenFailed)

    @ExceptionHandler(TokenVerifyException::class)
    fun refreshTokenVerifyException(e: TokenVerifyException) = failedOf<Void>(IErrorCode.RefreshTokenInvalid)

    @ExceptionHandler(PermissionDeniedException::class)
    fun permissionDeniedException(e: PermissionDeniedException) = failedOf<Void>(IErrorCode.PermissionDenied)

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun httpMessageNotReadableException(e: HttpMessageNotReadableException) =
        failedOf<Void>(IErrorCode.ParameterMissing)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidException(e: MethodArgumentNotValidException): RestValue<Void> {
        val message = e.bindingResult.allErrors.map {
            it.defaultMessage
        }.joinToString("; ")
        return failedOf(IErrorCode.ParameterVerificationFailed, message)
    }
    @ExceptionHandler
    fun exception(e: Exception): RestValue<Void> {
        log.error("Global exception handler catch exception", e)
        return failedOf(IErrorCode.Failed)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }

}
