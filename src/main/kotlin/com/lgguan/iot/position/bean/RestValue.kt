package com.lgguan.iot.position.bean

import com.fasterxml.jackson.databind.ObjectMapper

/**
 * Rest response body
 *
 * @author N.Liu
 **/
data class RestValue<T>(
    val code: Int,
    val message: String,
    val data: T? = null,
    val errorDetail: String? = null
) {
    override fun toString(): String {
        return ObjectMapper().writeValueAsString(this)
    }
}

fun <T> okOf(data: T? = null) = RestValue(IErrorCode.Success.code, IErrorCode.Success.message, data)

fun <T> failedOf(errorCode: IErrorCode = IErrorCode.Failed, errorDetail: String? = null, data: T? = null) =
    RestValue(errorCode.code, errorCode.message, data, errorDetail)

enum class IErrorCode(val code: Int, val message: String) {
    Success(200, "Request success"),
    Unauthorized(401, "Unauthorized"),
    PermissionDenied(403, "Permission denied"),
    Failed(500, "Request failed"),
    UsernamePasswordIncorrect(4001, "Username or password is incorrect"),
    UserExists(4002, "User already exists"),
    RefreshTokenInvalid(4003, "Refresh token is invalid"),
    ParameterMissing(4004, "Some request parameters may be missing"),
    ParameterVerificationFailed(4005, "Parameter verification failed"),
    DataExists(4006, "Data already exists"),
    DataNotExists(4007, "Data not exists"),
    GeneAccessTokenFailed(5001, "Generate access token failed")
}
