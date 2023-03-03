package com.lgguan.iot.position.exception

import com.lgguan.iot.position.bean.IErrorCode

/**
 *
 *
 * @author N.Liu
 **/

class TokenGenerateException: Throwable()
class TokenVerifyException: Throwable()
class PermissionDeniedException : Throwable()
class CustomException(errorCode: IErrorCode): Throwable(message = errorCode.message)
