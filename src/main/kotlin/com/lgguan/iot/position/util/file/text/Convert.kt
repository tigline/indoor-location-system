package com.lgguan.iot.position.util.file.text

import com.lgguan.iot.position.util.file.StringUtils
import java.math.BigDecimal
import java.math.BigInteger
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.text.NumberFormat
import java.util.*

/**
 * 类型转换器
 *
 */
object Convert {
    /**
     * 转换为字符串<br></br>
     * 如果给定的值为null，或者转换失败，返回默认值<br></br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    /**
     * 转换为字符串<br></br>
     * 如果给定的值为`null`，或者转换失败，返回默认值`null`<br></br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @return 结果
     */
    @JvmOverloads
    fun toStr(value: Any?, defaultValue: String? = null): String? {
        if (null == value) {
            return defaultValue
        }
        return if (value is String) {
            value
        } else value.toString()
    }
    /**
     * 转换为字符<br></br>
     * 如果给定的值为null，或者转换失败，返回默认值<br></br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    /**
     * 转换为字符<br></br>
     * 如果给定的值为`null`，或者转换失败，返回默认值`null`<br></br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @return 结果
     */
    @JvmOverloads
    fun toChar(value: Any?, defaultValue: Char? = null): Char? {
        if (null == value) {
            return defaultValue
        }
        if (value is Char) {
            return value
        }
        val valueStr = toStr(value, null)!!
        return if (StringUtils.isEmpty(valueStr)) defaultValue else valueStr!![0]
    }
    /**
     * 转换为byte<br></br>
     * 如果给定的值为`null`，或者转换失败，返回默认值<br></br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    /**
     * 转换为byte<br></br>
     * 如果给定的值为`null`，或者转换失败，返回默认值`null`<br></br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @return 结果
     */
    @JvmOverloads
    fun toByte(value: Any?, defaultValue: Byte? = null): Byte? {
        if (value == null) {
            return defaultValue
        }
        if (value is Byte) {
            return value
        }
        if (value is Number) {
            return value.toByte()
        }
        val valueStr = toStr(value, null)!!
        return if (StringUtils.isEmpty(valueStr)) {
            defaultValue
        } else try {
            valueStr!!.toByte()
        } catch (e: Exception) {
            defaultValue
        }
    }
    /**
     * 转换为Short<br></br>
     * 如果给定的值为`null`，或者转换失败，返回默认值<br></br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    /**
     * 转换为Short<br></br>
     * 如果给定的值为`null`，或者转换失败，返回默认值`null`<br></br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @return 结果
     */
    @JvmOverloads
    fun toShort(value: Any?, defaultValue: Short? = null): Short? {
        if (value == null) {
            return defaultValue
        }
        if (value is Short) {
            return value
        }
        if (value is Number) {
            return value.toShort()
        }
        val valueStr = toStr(value, null)!!
        return if (StringUtils.isEmpty(valueStr)) {
            defaultValue
        } else try {
            valueStr!!.trim { it <= ' ' }.toShort()
        } catch (e: Exception) {
            defaultValue
        }
    }
    /**
     * 转换为Number<br></br>
     * 如果给定的值为空，或者转换失败，返回默认值<br></br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    /**
     * 转换为Number<br></br>
     * 如果给定的值为空，或者转换失败，返回默认值`null`<br></br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @return 结果
     */
    @JvmOverloads
    fun toNumber(value: Any?, defaultValue: Number? = null): Number? {
        if (value == null) {
            return defaultValue
        }
        if (value is Number) {
            return value
        }
        val valueStr = toStr(value, null)!!
        return if (StringUtils.isEmpty(valueStr)) {
            defaultValue
        } else try {
            NumberFormat.getInstance().parse(valueStr)
        } catch (e: Exception) {
            defaultValue
        }
    }
    /**
     * 转换为int<br></br>
     * 如果给定的值为空，或者转换失败，返回默认值<br></br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    /**
     * 转换为int<br></br>
     * 如果给定的值为`null`，或者转换失败，返回默认值`null`<br></br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @return 结果
     */
    @JvmOverloads
    fun toInt(value: Any?, defaultValue: Int? = null): Int? {
        if (value == null) {
            return defaultValue
        }
        if (value is Int) {
            return value
        }
        if (value is Number) {
            return value.toInt()
        }
        val valueStr = toStr(value, null)!!
        return if (StringUtils.isEmpty(valueStr)) {
            defaultValue
        } else try {
            valueStr!!.trim { it <= ' ' }.toInt()
        } catch (e: Exception) {
            defaultValue
        }
    }

    /**
     * 转换为Integer数组<br></br>
     *
     * @param str 被转换的值
     * @return 结果
     */
    fun toIntArray(str: String): Array<Int?> {
        return toIntArray(",", str)
    }

    /**
     * 转换为Long数组<br></br>
     *
     * @param str 被转换的值
     * @return 结果
     */
    fun toLongArray(str: String): Array<Long?> {
        return toLongArray(",", str)
    }

    /**
     * 转换为Integer数组<br></br>
     *
     * @param split 分隔符
     * @param str 被转换的值
     * @return 结果
     */
    fun toIntArray(split: String, str: String): Array<Int?> {
        if (StringUtils.isEmpty(str)) {
            return arrayOf()
        }
        val arr = str.split(split.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val ints = arrayOfNulls<Int>(arr.size)
        for (i in arr.indices) {
            val v = toInt(arr[i], 0)
            ints[i] = v
        }
        return ints
    }

    /**
     * 转换为Long数组<br></br>
     *
     * @param split 分隔符
     * @param str 被转换的值
     * @return 结果
     */
    fun toLongArray(split: String, str: String): Array<Long?> {
        if (StringUtils.isEmpty(str)) {
            return arrayOf()
        }
        val arr = str.split(split.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val longs = arrayOfNulls<Long>(arr.size)
        for (i in arr.indices) {
            val v = toLong(arr[i], null)
            longs[i] = v
        }
        return longs
    }

    /**
     * 转换为String数组<br></br>
     *
     * @param str 被转换的值
     * @return 结果
     */
    fun toStrArray(str: String): Array<String> {
        return toStrArray(",", str)
    }

    /**
     * 转换为String数组<br></br>
     *
     * @param split 分隔符
     * @param str 被转换的值
     * @return 结果
     */
    fun toStrArray(split: String, str: String): Array<String> {
        return str.split(split.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    }
    /**
     * 转换为long<br></br>
     * 如果给定的值为空，或者转换失败，返回默认值<br></br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    /**
     * 转换为long<br></br>
     * 如果给定的值为`null`，或者转换失败，返回默认值`null`<br></br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @return 结果
     */
    @JvmOverloads
    fun toLong(value: Any?, defaultValue: Long? = null): Long? {
        if (value == null) {
            return defaultValue
        }
        if (value is Long) {
            return value
        }
        if (value is Number) {
            return value.toLong()
        }
        val valueStr = toStr(value, null)!!
        return if (StringUtils.isEmpty(valueStr)) {
            defaultValue
        } else try {
            // 支持科学计数法
            BigDecimal(valueStr!!.trim { it <= ' ' }).toLong()
        } catch (e: Exception) {
            defaultValue
        }
    }
    /**
     * 转换为double<br></br>
     * 如果给定的值为空，或者转换失败，返回默认值<br></br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    /**
     * 转换为double<br></br>
     * 如果给定的值为空，或者转换失败，返回默认值`null`<br></br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @return 结果
     */
    @JvmOverloads
    fun toDouble(value: Any?, defaultValue: Double? = null): Double? {
        if (value == null) {
            return defaultValue
        }
        if (value is Double) {
            return value
        }
        if (value is Number) {
            return value.toDouble()
        }
        val valueStr = toStr(value, null)!!
        return if (StringUtils.isEmpty(valueStr)) {
            defaultValue
        } else try {
            // 支持科学计数法
            BigDecimal(valueStr!!.trim { it <= ' ' }).toDouble()
        } catch (e: Exception) {
            defaultValue
        }
    }
    /**
     * 转换为Float<br></br>
     * 如果给定的值为空，或者转换失败，返回默认值<br></br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    /**
     * 转换为Float<br></br>
     * 如果给定的值为空，或者转换失败，返回默认值`null`<br></br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @return 结果
     */
    @JvmOverloads
    fun toFloat(value: Any?, defaultValue: Float? = null): Float? {
        if (value == null) {
            return defaultValue
        }
        if (value is Float) {
            return value
        }
        if (value is Number) {
            return value.toFloat()
        }
        val valueStr = toStr(value, null)!!
        return if (StringUtils.isEmpty(valueStr)) {
            defaultValue
        } else try {
            valueStr!!.trim { it <= ' ' }.toFloat()
        } catch (e: Exception) {
            defaultValue
        }
    }
    /**
     * 转换为boolean<br></br>
     * String支持的值为：true、false、yes、ok、no，1,0 如果给定的值为空，或者转换失败，返回默认值<br></br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    /**
     * 转换为boolean<br></br>
     * 如果给定的值为空，或者转换失败，返回默认值`null`<br></br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @return 结果
     */
    @JvmOverloads
    fun toBool(value: Any?, defaultValue: Boolean? = null): Boolean? {
        if (value == null) {
            return defaultValue
        }
        if (value is Boolean) {
            return value
        }
        var valueStr = toStr(value, null)!!
        if (StringUtils.isEmpty(valueStr)) {
            return defaultValue
        }
        valueStr = valueStr!!.trim { it <= ' ' }.lowercase(Locale.getDefault())
        return when (valueStr) {
            "true", "yes", "ok", "1" -> true
            "false", "no", "0" -> false
            else -> defaultValue
        }
    }

    /**
     * 转换为Enum对象<br></br>
     * 如果给定的值为空，或者转换失败，返回默认值<br></br>
     *
     * @param clazz Enum的Class
     * @param value 值
     * @param defaultValue 默认值
     * @return Enum
     */
    fun <E : Enum<E>?> toEnum(clazz: Class<E>, value: Any?, defaultValue: E): E {
        if (value == null) {
            return defaultValue
        }
        if (clazz.isAssignableFrom(value.javaClass)) {
            return value as E
        }
        val valueStr = toStr(value, null)!!
        return if (StringUtils.isEmpty(valueStr)) {
            defaultValue
        } else try {
            java.lang.Enum.valueOf(clazz, valueStr)
        } catch (e: Exception) {
            defaultValue
        }
    }

    /**
     * 转换为BigInteger<br></br>
     * 如果给定的值为空，或者转换失败，返回默认值<br></br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    /**
     * 转换为BigInteger<br></br>
     * 如果给定的值为空，或者转换失败，返回默认值`null`<br></br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @return 结果
     */
    @JvmOverloads
    fun toBigInteger(value: Any?, defaultValue: BigInteger? = null): BigInteger? {
        if (value == null) {
            return defaultValue
        }
        if (value is BigInteger) {
            return value
        }
        if (value is Long) {
            return BigInteger.valueOf(value)
        }
        val valueStr = toStr(value, null)!!
        return if (StringUtils.isEmpty(valueStr)) {
            defaultValue
        } else try {
            BigInteger(valueStr)
        } catch (e: Exception) {
            defaultValue
        }
    }
    /**
     * 转换为BigDecimal<br></br>
     * 如果给定的值为空，或者转换失败，返回默认值<br></br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    /**
     * 转换为BigDecimal<br></br>
     * 如果给定的值为空，或者转换失败，返回默认值<br></br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @return 结果
     */
    @JvmOverloads
    fun toBigDecimal(value: Any?, defaultValue: BigDecimal? = null): BigDecimal? {
        if (value == null) {
            return defaultValue
        }
        if (value is BigDecimal) {
            return value as BigDecimal?
        }
        if (value is Long) {
            return BigDecimal(value)
        }
        if (value is Double) {
            return BigDecimal.valueOf(value)
        }
        if (value is Int) {
            return BigDecimal(value)
        }
        val valueStr = toStr(value, null)!!
        return if (StringUtils.isEmpty(valueStr)) {
            defaultValue
        } else try {
            BigDecimal(valueStr)
        } catch (e: Exception) {
            defaultValue
        }
    }

    /**
     * 将对象转为字符串<br></br>
     * 1、Byte数组和ByteBuffer会被转换为对应字符串的数组 2、对象数组会调用Arrays.toString方法
     *
     * @param obj 对象
     * @return 字符串
     */
    fun utf8Str(obj: Any?): String? {
        return str(obj, CharsetKit.CHARSET_UTF_8)
    }

    /**
     * 将对象转为字符串<br></br>
     * 1、Byte数组和ByteBuffer会被转换为对应字符串的数组 2、对象数组会调用Arrays.toString方法
     *
     * @param obj 对象
     * @param charsetName 字符集
     * @return 字符串
     */
    fun str(obj: Any?, charsetName: String?): String? {
        return str(obj, Charset.forName(charsetName))
    }

    /**
     * 将对象转为字符串<br></br>
     * 1、Byte数组和ByteBuffer会被转换为对应字符串的数组 2、对象数组会调用Arrays.toString方法
     *
     * @param obj 对象
     * @param charset 字符集
     * @return 字符串
     */
    open fun str(obj: Any?, charset: Charset?): String? {
        if (null == obj) {
            return null
        }
        if (obj is String) {
            return obj
        } else if (obj is ByteArray) {
            return str(obj as ByteArray?, charset)
        } else if (obj is ByteBuffer) {
            return str(obj as ByteBuffer?, charset)
        }
        return obj.toString()
    }

    /**
     * 将byte数组转为字符串
     *
     * @param bytes byte数组
     * @param charset 字符集
     * @return 字符串
     */
    fun str(bytes: ByteArray?, charset: String): String? {
        return str(bytes, if (StringUtils.isEmpty(charset)) Charset.defaultCharset() else Charset.forName(charset))
    }

    /**
     * 解码字节码
     *
     * @param data 字符串
     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
     * @return 解码后的字符串
     */
    fun str(data: ByteArray?, charset: Charset?): String? {
        return if (data == null) {
            null
        } else charset?.let { String(data, it) } ?: String(data)
    }

    /**
     * 将编码的byteBuffer数据转换为字符串
     *
     * @param data 数据
     * @param charset 字符集，如果为空使用当前系统字符集
     * @return 字符串
     */
    fun str(data: ByteBuffer?, charset: String?): String? {
        return if (data == null) {
            null
        } else str(data, Charset.forName(charset))
    }

    /**
     * 将编码的byteBuffer数据转换为字符串
     *
     * @param data 数据
     * @param charset 字符集，如果为空使用当前系统字符集
     * @return 字符串
     */
    fun str(data: ByteBuffer?, charset: Charset?): String {
        var charset = charset
        if (null == charset) {
            charset = Charset.defaultCharset()
        }
        return charset!!.decode(data).toString()
    }
    /**
     * 半角转全角
     *
     * @param input String
     * @param notConvertSet 不替换的字符集合
     * @return 全角字符串.
     */
    // ----------------------------------------------------------------------- 全角半角转换
    /**
     * 半角转全角
     *
     * @param input String.
     * @return 全角字符串.
     */
    @JvmOverloads
    fun toSBC(input: String, notConvertSet: Set<Char?>? = null): String {
        val c = input.toCharArray()
        for (i in c.indices) {
            if (null != notConvertSet && notConvertSet.contains(c[i])) {
                // 跳过不替换的字符
                continue
            }
            if (c[i] == ' ') {
                c[i] = '\u3000'
            } else if (c[i] < '\u007f') {
                c[i] = (c[i].code + 65248).toChar()
            }
        }
        return String(c)
    }

    /**
     * 全角转半角
     *
     * @param input String.
     * @return 半角字符串
     */
    fun toDBC(input: String): String {
        return toDBC(input, null)
    }

    /**
     * 替换全角为半角
     *
     * @param text 文本
     * @param notConvertSet 不替换的字符集合
     * @return 替换后的字符
     */
    fun toDBC(text: String, notConvertSet: Set<Char?>?): String {
        val c = text.toCharArray()
        for (i in c.indices) {
            if (null != notConvertSet && notConvertSet.contains(c[i])) {
                // 跳过不替换的字符
                continue
            }
            if (c[i] == '\u3000') {
                c[i] = ' '
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                c[i] = (c[i].code - 65248).toChar()
            }
        }
        return String(c)
    }

    /**
     * 数字金额大写转换 先写个完整的然后将如零拾替换成零
     *
     * @param n 数字
     * @return 中文大写数字
     */
    fun digitUppercase(n: Double): String {
        var n = n
        val fraction = arrayOf("角", "分")
        val digit = arrayOf("零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖")
        val unit = arrayOf(arrayOf("元", "万", "亿"), arrayOf("", "拾", "佰", "仟"))
        val head = if (n < 0) "负" else ""
        n = Math.abs(n)
        var s = ""
        for (i in fraction.indices) {
            s += (digit[(Math.floor(
                n * 10 * Math.pow(
                    10.0,
                    i.toDouble()
                )
            ) % 10).toInt()] + fraction[i]).replace("(零.)+".toRegex(), "")
        }
        if (s.length < 1) {
            s = "整"
        }
        var integerPart = Math.floor(n).toInt()
        var i = 0
        while (i < unit[0].size && integerPart > 0) {
            var p = ""
            var j = 0
            while (j < unit[1].size && n > 0) {
                p = digit[integerPart % 10] + unit[1][j] + p
                integerPart = integerPart / 10
                j++
            }
            s = p.replace("(零.)*零$".toRegex(), "").replace("^$".toRegex(), "零") + unit[0][i] + s
            i++
        }
        return head + s.replace("(零.)*零元".toRegex(), "元").replaceFirst("(零.)+".toRegex(), "")
            .replace("(零.)+".toRegex(), "零").replace("^整$".toRegex(), "零元整")
    }
}