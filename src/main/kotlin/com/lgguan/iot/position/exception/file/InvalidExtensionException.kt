package com.lgguan.iot.position.exception.file

import java.util.*

/**
 * 文件上传 误异常类
 *
 */
open class InvalidExtensionException(val allowedExtension: Array<String>, val extension: String, val filename: String) :
    FileUploadException(
        "filename : [$filename], extension : [$extension], allowed extension : [" + Arrays.toString(
            allowedExtension
        ) + "]"
    ) {

    class InvalidImageExtensionException(allowedExtension: Array<String>, extension: String, filename: String) :
        InvalidExtensionException(allowedExtension, extension, filename) {
        companion object {
            private const val serialVersionUID = 1L
        }
    }

    class InvalidFlashExtensionException(allowedExtension: Array<String>, extension: String, filename: String) :
        InvalidExtensionException(allowedExtension, extension, filename) {
        companion object {
            private const val serialVersionUID = 1L
        }
    }

    class InvalidMediaExtensionException(allowedExtension: Array<String>, extension: String, filename: String) :
        InvalidExtensionException(allowedExtension, extension, filename) {
        companion object {
            private const val serialVersionUID = 1L
        }
    }

    class InvalidVideoExtensionException(allowedExtension: Array<String>, extension: String, filename: String) :
        InvalidExtensionException(allowedExtension, extension, filename) {
        companion object {
            private const val serialVersionUID = 1L
        }
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}