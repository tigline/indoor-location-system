package com.lgguan.iot.position.util.file

import com.lgguan.iot.position.util.file.text.StrFormatter
import org.apache.commons.lang3.StringUtils
import org.springframework.util.AntPathMatcher
import java.util.*

/**
 * 字符串工具类
 *
 */
object StringUtils : StringUtils() {
    /**
     * http请求
     */
    val HTTP = "http://"

    /**
     * https请求
     */
    val HTTPS = "https://"

    /** 空字符串  */
    private const val NULLSTR = ""

    /** 下划线  */
    private const val SEPARATOR = '_'

    /**
     * 获取参数不为空值
     *
     * @param value defaultValue 要判断的value
     * @return value 返回值
     */
    fun <T> nvl(value: T?, defaultValue: T): T {
        return value ?: defaultValue
    }

    /**
     * * 判断一个Collection是否为空， 包含List，Set，Queue
     *
     * @param coll 要判断的Collection
     * @return true：为空 false：非空
     */
    fun isEmpty(coll: Collection<*>): Boolean {
        return isNull(coll) || coll.isEmpty()
    }

    /**
     * * 判断一个Collection是否非空，包含List，Set，Queue
     *
     * @param coll 要判断的Collection
     * @return true：非空 false：空
     */
    fun isNotEmpty(coll: Collection<*>): Boolean {
        return !isEmpty(coll)
    }

    /**
     * * 判断一个对象数组是否为空
     *
     * @param objects 要判断的对象数组
     * @return true：为空 false：非空
     */
    fun isEmpty(objects: Array<out Any?>): Boolean {
        return isNull(objects) || objects.size == 0
    }

    /**
     * * 判断一个对象数组是否非空
     *
     * @param objects 要判断的对象数组
     * @return true：非空 false：空
     */
    fun isNotEmpty(objects: Array<Any>): Boolean {
        return !isEmpty(objects)
    }

    /**
     * * 判断一个Map是否为空
     *
     * @param map 要判断的Map
     * @return true：为空 false：非空
     */
    fun isEmpty(map: Map<*, *>): Boolean {
        return isNull(map) || map.isEmpty()
    }

    /**
     * * 判断一个Map是否为空
     *
     * @param map 要判断的Map
     * @return true：非空 false：空
     */
    fun isNotEmpty(map: Map<*, *>): Boolean {
        return !isEmpty(map)
    }

    /**
     * * 判断一个字符串是否为空串
     *
     * @param str String
     * @return true：为空 false：非空
     */
    fun isEmpty(str: String): Boolean {
        return isNull(str) || NULLSTR == str.trim { it <= ' ' }
    }

    /**
     * * 判断一个字符串是否为非空串
     *
     * @param str String
     * @return true：非空串 false：空串
     */
    fun isNotEmpty(str: String): Boolean {
        return !isEmpty(str)
    }

    /**
     * * 判断一个对象是否为空
     *
     * @param object Object
     * @return true：为空 false：非空
     */
    fun isNull(`object`: Any?): Boolean {
        return `object` == null
    }

    /**
     * * 判断一个对象是否非空
     *
     * @param object Object
     * @return true：非空 false：空
     */
    fun isNotNull(`object`: Any?): Boolean {
        return !isNull(`object`)
    }

    /**
     * * 判断一个对象是否是数组类型（Java基本型别的数组）
     *
     * @param object 对象
     * @return true：是数组 false：不是数组
     */
    fun isArray(`object`: Any): Boolean {
        return isNotNull(`object`) && `object`.javaClass.isArray
    }

    /**
     * 判断是否为空，并且不是空白字符
     *
     * @param str 要判断的value
     * @return 结果
     */
    fun hasText(str: String?): Boolean {
        return str != null && !str.isEmpty() && containsText(str)
    }

    private fun containsText(str: CharSequence): Boolean {
        val strLen = str.length
        for (i in 0 until strLen) {
            if (!Character.isWhitespace(str[i])) {
                return true
            }
        }
        return false
    }

    /**
     * 格式化文本, {} 表示占位符<br></br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br></br>
     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br></br>
     * 例：<br></br>
     * 通常使用：format("this is {} for {}", "a", "b") -> this is a for b<br></br>
     * 转义{}： format("this is \\{} for {}", "a", "b") -> this is \{} for a<br></br>
     * 转义\： format("this is \\\\{} for {}", "a", "b") -> this is \a for b<br></br>
     *
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param params 参数值
     * @return 格式化后的文本
     */
    fun format(template: String, vararg params: Any?): String {
        return if (isEmpty(params) || isEmpty(template)
        ) {
            template
        } else StrFormatter.format(template, params)
    }

    /**
     * 是否为http(s)://开头
     *
     * @param link 链接
     * @return 结果
     */
    fun ishttp(link: String?): Boolean {
        return startsWithAny(link, HTTP, HTTPS)
    }

    /**
     * 判断给定的collection列表中是否包含数组array 判断给定的数组array中是否包含给定的元素value
     *
     * @param collection 给定的集合
     * @param array 给定的数组
     * @return boolean 结果
     */
    fun containsAny(collection: Collection<String?>, vararg array: String?): Boolean {
        return if (isEmpty(collection) || isEmpty(array)) {
            false
        } else {
            for (str in array) {
                if (collection.contains(str)) {
                    return true
                }
            }
            false
        }
    }

    /**
     * 驼峰转下划线命名
     */
    fun toUnderScoreCase(str: String?): String? {
        if (str == null) {
            return null
        }
        val sb = StringBuilder()
        // 前置字符是否大写
        var preCharIsUpperCase = true
        // 当前字符是否大写
        var curreCharIsUpperCase = true
        // 下一字符是否大写
        var nexteCharIsUpperCase = true
        for (i in 0 until str.length) {
            val c = str[i]
            preCharIsUpperCase = if (i > 0) {
                Character.isUpperCase(str[i - 1])
            } else {
                false
            }
            curreCharIsUpperCase = Character.isUpperCase(c)
            if (i < str.length - 1) {
                nexteCharIsUpperCase = Character.isUpperCase(str[i + 1])
            }
            if (preCharIsUpperCase && curreCharIsUpperCase && !nexteCharIsUpperCase) {
                sb.append(SEPARATOR)
            } else if (i != 0 && !preCharIsUpperCase && curreCharIsUpperCase) {
                sb.append(SEPARATOR)
            }
            sb.append(c.lowercaseChar())
        }
        return sb.toString()
    }

    /**
     * 是否包含字符串
     *
     * @param str 验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    fun inStringIgnoreCase(str: String?, vararg strs: String?): Boolean {
        if (str != null && strs != null) {
            for (s in strs) {
                if (str.equals(trim(s), ignoreCase = true)) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。 例如：HELLO_WORLD->HelloWorld
     *
     * @param name 转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    fun convertToCamelCase(name: String?): String {
        val result = StringBuilder()
        // 快速检查
        if (name == null || name.isEmpty()) {
            // 没必要转换
            return ""
        } else if (!name.contains("_")) {
            // 不含下划线，仅将首字母大写
            return name.substring(0, 1).uppercase(Locale.getDefault()) + name.substring(1)
        }
        // 用下划线将原始字符串分割
        val camels = name.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (camel in camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue
            }
            // 首字母大写
            result.append(camel.substring(0, 1).uppercase(Locale.getDefault()))
            result.append(camel.substring(1).lowercase(Locale.getDefault()))
        }
        return result.toString()
    }

    /**
     * 驼峰式命名法
     * 例如：user_name->userName
     */
    fun toCamelCase(s: String?): String? {
        var s = s ?: return null
        if (s.indexOf(SEPARATOR) == -1) {
            return s
        }
        s = s.lowercase(Locale.getDefault())
        val sb = StringBuilder(s.length)
        var upperCase = false
        for (i in 0 until s.length) {
            val c = s[i]
            if (c == SEPARATOR) {
                upperCase = true
            } else if (upperCase) {
                sb.append(c.uppercaseChar())
                upperCase = false
            } else {
                sb.append(c)
            }
        }
        return sb.toString()
    }

    /**
     * 查找指定字符串是否匹配指定字符串列表中的任意一个字符串
     *
     * @param str 指定字符串
     * @param strs 需要检查的字符串数组
     * @return 是否匹配
     */
    fun matches(str: String, strs: List<String?>): Boolean {
        if (isEmpty(str) || isEmpty(strs)) {
            return false
        }
        for (pattern in strs) {
            if (isMatch(pattern, str)) {
                return true
            }
        }
        return false
    }

    /**
     * 判断url是否与规则配置:
     * ? 表示单个字符;
     * * 表示一层路径内的任意字符串，不可跨层级;
     * ** 表示任意层路径;
     *
     * @param pattern 匹配规则
     * @param url 需要匹配的url
     * @return
     */
    fun isMatch(pattern: String?, url: String?): Boolean {
        val matcher = AntPathMatcher()
        return matcher.match(pattern!!, url!!)
    }

    fun <T> cast(obj: Any): T {
        return obj as T
    }

    /**
     * 数字左边补齐0，使之达到指定长度。注意，如果数字转换为字符串后，长度大于size，则只保留 最后size个字符。
     *
     * @param num 数字对象
     * @param size 字符串指定长度
     * @return 返回数字的字符串格式，该字符串为指定长度。
     */
    fun padl(num: Number, size: Int): String {
        return padl(num.toString(), size, '0')
    }

    /**
     * 字符串左补齐。如果原始字符串s长度大于size，则只保留最后size个字符。
     *
     * @param s 原始字符串
     * @param size 字符串指定长度
     * @param c 用于补齐的字符
     * @return 返回指定长度的字符串，由原字符串左补齐或截取得到。
     */
    fun padl(s: String?, size: Int, c: Char): String {
        val sb = StringBuilder(size)
        if (s != null) {
            val len = s.length
            if (s.length <= size) {
                for (i in size - len downTo 1) {
                    sb.append(c)
                }
                sb.append(s)
            } else {
                return s.substring(len - size, len)
            }
        } else {
            for (i in size downTo 1) {
                sb.append(c)
            }
        }
        return sb.toString()
    }

}