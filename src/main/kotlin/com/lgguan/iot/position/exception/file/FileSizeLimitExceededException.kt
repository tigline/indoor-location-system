package com.lgguan.iot.position.exception.file

/**
 * 文件名大小限制异常类
 *
 */
class FileSizeLimitExceededException(defaultMaxSize: Long) :
    FileException("upload.exceed.maxSize", arrayOf<Any>(defaultMaxSize), "the filesize is too large") {
    companion object {
        private const val serialVersionUID = 1L
    }
}