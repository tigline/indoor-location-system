package com.lgguan.iot.position.exception.file

/**
 * 文件名称超长限制异常类
 *
 */
class FileNameLengthLimitExceededException(defaultFileNameLength: Int) :
    FileException("upload.filename.exceed.length", arrayOf<Any>(defaultFileNameLength), "the filename is too long") {
    companion object {
        private const val serialVersionUID = 1L
    }
}