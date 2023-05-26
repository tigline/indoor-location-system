package com.lgguan.iot.position.exception.file

import java.io.PrintStream
import java.io.PrintWriter

/**
 * 文件上传异常类
 *
 */
open class FileUploadException @JvmOverloads constructor(msg: String? = null, cause: Throwable? = null) :
    Exception(msg) {
    override val cause: Throwable

    init {
        this.cause = cause!!
    }

    override fun printStackTrace(stream: PrintStream) {
        super.printStackTrace(stream)
        if (cause != null) {
            stream.println("Caused by:")
            cause.printStackTrace(stream)
        }
    }

    override fun printStackTrace(writer: PrintWriter) {
        super.printStackTrace(writer)
        if (cause != null) {
            writer.println("Caused by:")
            cause.printStackTrace(writer)
        }
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}