package com.lgguan.iot.position.util.file

import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.util.*

/**
 * 文件类型工具类
 *
 */
object FileTypeUtils {
    /**
     * 获取文件类型
     *
     *
     * 例如: ruoyi.txt, 返回: txt
     *
     * @param file 文件名
     * @return 后缀（不含".")
     */
    fun getFileType(file: File?): String {
        return if (null == file) {
            StringUtils.EMPTY
        } else getFileType(file.name)
    }

    /**
     * 获取文件类型
     *
     *
     * 例如: ruoyi.txt, 返回: txt
     *
     * @param fileName 文件名
     * @return 后缀（不含".")
     */
    fun getFileType(fileName: String): String {
        val separatorIndex = fileName.lastIndexOf(".")
        return if (separatorIndex < 0) {
            ""
        } else fileName.substring(separatorIndex + 1).lowercase(Locale.getDefault())
    }

    /**
     * 获取文件名的后缀
     *
     * @param file 表单文件
     * @return 后缀名
     */
    fun getExtension(file: MultipartFile): String {
        var extension = FilenameUtils.getExtension(file.originalFilename)
        if (StringUtils.isEmpty(extension)) {
            extension = MimeTypeUtils.getExtension(Objects.requireNonNull(file.contentType))
        }
        return extension
    }
}