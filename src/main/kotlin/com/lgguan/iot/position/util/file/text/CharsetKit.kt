package com.lgguan.iot.position.util.file.text

import com.lgguan.iot.position.util.file.StringUtils
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

/**
 * 字符集工具类
 *
 */
object CharsetKit {
    /** ISO-8859-1  */
    const val ISO_8859_1 = "ISO-8859-1"

    /** UTF-8  */
    const val UTF_8 = "UTF-8"

    /** GBK  */
    const val GBK = "GBK"

    /** ISO-8859-1  */
    val CHARSET_ISO_8859_1 = Charset.forName(ISO_8859_1)

    /** UTF-8  */
    val CHARSET_UTF_8 = Charset.forName(UTF_8)

    /** GBK  */
    val CHARSET_GBK = Charset.forName(GBK)

    /**
     * 转换为Charset对象
     *
     * @param charset 字符集，为空则返回默认字符集
     * @return Charset
     */
    fun charset(charset: String): Charset {
        return if (StringUtils.isEmpty(charset)) Charset.defaultCharset() else Charset.forName(charset)
    }

    /**
     * 转换字符串的字符集编码
     *
     * @param source 字符串
     * @param srcCharset 源字符集，默认ISO-8859-1
     * @param destCharset 目标字符集，默认UTF-8
     * @return 转换后的字符集
     */
    fun convert(source: String, srcCharset: String?, destCharset: String?): String {
        return convert(source, Charset.forName(srcCharset), Charset.forName(destCharset))
    }

    /**
     * 转换字符串的字符集编码
     *
     * @param source 字符串
     * @param srcCharset 源字符集，默认ISO-8859-1
     * @param destCharset 目标字符集，默认UTF-8
     * @return 转换后的字符集
     */
    fun convert(source: String, srcCharset: Charset?, destCharset: Charset?): String {
        var srcCharset = srcCharset
        var destCharset = destCharset
        if (null == srcCharset) {
            srcCharset = StandardCharsets.ISO_8859_1
        }
        if (null == destCharset) {
            destCharset = StandardCharsets.UTF_8
        }
        return if (StringUtils.isEmpty(source) || srcCharset == destCharset) {
            source
        } else String(source.toByteArray(srcCharset!!), destCharset!!)
    }

    /**
     * @return 系统字符集编码
     */
    fun systemCharset(): String {
        return Charset.defaultCharset().name()
    }
}