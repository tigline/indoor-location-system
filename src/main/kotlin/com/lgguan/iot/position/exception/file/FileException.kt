package com.lgguan.iot.position.exception.file

import com.lgguan.iot.position.exception.BaseException

/**
 * 文件信息异常类
 *
 */
open class FileException(code: String?, args: Array<Any>?, msg: String?) : BaseException("file", code, args, msg) {
    companion object {
        private const val serialVersionUID = 1L
    }
}