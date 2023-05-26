package com.lgguan.iot.position.util.file

import jakarta.servlet.http.HttpServletResponse
import org.apache.commons.lang3.ArrayUtils
import org.apache.commons.lang3.StringUtils
import java.io.*
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * 文件处理工具类
 *
 */
object FileUtils {
    /** 字符常量：斜杠 `'/'`  */
    const val SLASH = '/'

    /** 字符常量：反斜杠 `'\\'`  */
    const val BACKSLASH = '\\'
    var FILENAME_PATTERN = "[a-zA-Z0-9_\\-\\|\\.\\u4e00-\\u9fa5]+"

    /**
     * 输出指定文件的byte数组
     *
     * @param filePath 文件路径
     * @param os 输出流
     * @return
     */
    @Throws(IOException::class)
    fun writeBytes(filePath: String?, os: OutputStream?) {
        var fis: FileInputStream? = null
        try {
            val file = File(filePath)
            if (!file.exists()) {
                throw FileNotFoundException(filePath)
            }
            fis = FileInputStream(file)
            val b = ByteArray(1024)
            var length: Int
            while (fis.read(b).also { length = it } > 0) {
                os!!.write(b, 0, length)
            }
        } catch (e: IOException) {
            throw e
        } finally {
            if (os != null) {
                try {
                    os.close()
                } catch (e1: IOException) {
                    e1.printStackTrace()
                }
            }
            if (fis != null) {
                try {
                    fis.close()
                } catch (e1: IOException) {
                    e1.printStackTrace()
                }
            }
        }
    }

    /**
     * 删除文件
     *
     * @param filePath 文件
     * @return
     */
    fun deleteFile(filePath: String?): Boolean {
        var flag = false
        val file = File(filePath)
        // 路径为文件且不为空则进行删除
        if (file.isFile && file.exists()) {
            flag = file.delete()
        }
        return flag
    }

    /**
     * 检查文件是否可下载
     *
     * @param resource 需要下载的文件
     * @return true 正常 false 非法
     */
    fun checkAllowDownload(resource: String): Boolean {
        // 禁止目录上跳级别
        return if (StringUtils.contains(resource, "..")) {
            false
        } else ArrayUtils.contains(
            MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION,
            FileTypeUtils.getFileType(resource)
        )
        // 判断是否在允许下载的文件规则内
    }

    /**
     * 返回文件名
     *
     * @param filePath 文件
     * @return 文件名
     */
    fun getName(filePath: String?): String? {
        if (null == filePath) {
            return null
        }
        var len = filePath.length
        if (0 == len) {
            return filePath
        }
        if (isFileSeparator(filePath[len - 1])) {
            // 以分隔符结尾的去掉结尾分隔符
            len--
        }
        var begin = 0
        var c: Char
        for (i in len - 1 downTo -1 + 1) {
            c = filePath[i]
            if (isFileSeparator(c)) {
                // 查找最后一个路径分隔符（/或者\）
                begin = i + 1
                break
            }
        }
        return filePath.substring(begin, len)
    }

    /**
     * 是否为Windows或者Linux（Unix）文件分隔符<br></br>
     * Windows平台下分隔符为\，Linux（Unix）为/
     *
     * @param c 字符
     * @return 是否为Windows或者Linux（Unix）文件分隔符
     */
    fun isFileSeparator(c: Char): Boolean {
        return SLASH == c || BACKSLASH == c
    }

    /**
     * 下载文件名重新编码
     *
     * @param response 响应对象
     * @param realFileName 真实文件名
     * @return
     */
    @Throws(UnsupportedEncodingException::class)
    fun setAttachmentResponseHeader(response: HttpServletResponse, realFileName: String?) {
        val percentEncodedFileName = percentEncode(realFileName)
        val contentDispositionValue = StringBuilder()
        contentDispositionValue.append("attachment; filename=")
            .append(percentEncodedFileName)
            .append(";")
            .append("filename*=")
            .append("utf-8''")
            .append(percentEncodedFileName)
        response.setHeader("Content-disposition", contentDispositionValue.toString())
        response.setHeader("download-filename", percentEncodedFileName)
    }

    /**
     * 百分号编码工具方法
     *
     * @param s 需要百分号编码的字符串
     * @return 百分号编码后的字符串
     */
    @Throws(UnsupportedEncodingException::class)
    fun percentEncode(s: String?): String {
        val encode = URLEncoder.encode(s, StandardCharsets.UTF_8.toString())
        return encode.replace("\\+".toRegex(), "%20")
    }
}