package com.lgguan.iot.position.util.file.text

import com.lgguan.iot.position.util.file.StringUtils

/**
 * 字符串格式化
 *
 */
object StrFormatter {
    const val EMPTY_JSON = "{}"
    const val C_BACKSLASH = '\\'
    const val C_DELIM_START = '{'
    const val C_DELIM_END = '}'

    /**
     * 格式化字符串<br></br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br></br>
     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br></br>
     * 例：<br></br>
     * 通常使用：format("this is {} for {}", "a", "b") -> this is a for b<br></br>
     * 转义{}： format("this is \\{} for {}", "a", "b") -> this is \{} for a<br></br>
     * 转义\： format("this is \\\\{} for {}", "a", "b") -> this is \a for b<br></br>
     *
     * @param strPattern 字符串模板
     * @param argArray 参数列表
     * @return 结果
     */
    fun format(strPattern: String, vararg argArray: Any?): String {
        if (StringUtils.isEmpty(strPattern) || StringUtils.isEmpty(argArray)) {
            return strPattern
        }
        val strPatternLength = strPattern.length

        // 初始化定义好的长度以获得更好的性能
        val sbuf = StringBuilder(strPatternLength + 50)
        var handledPosition = 0
        var delimIndex: Int // 占位符所在位置
        var argIndex = 0
        while (argIndex < argArray.size) {
            delimIndex = strPattern.indexOf(EMPTY_JSON, handledPosition)
            handledPosition = if (delimIndex == -1) {
                return if (handledPosition == 0) {
                    strPattern
                } else { // 字符串模板剩余部分不再包含占位符，加入剩余部分后返回结果
                    sbuf.append(strPattern, handledPosition, strPatternLength)
                    sbuf.toString()
                }
            } else {
                if (delimIndex > 0 && strPattern[delimIndex - 1] == C_BACKSLASH) {
                    if (delimIndex > 1 && strPattern[delimIndex - 2] == C_BACKSLASH) {
                        // 转义符之前还有一个转义符，占位符依旧有效
                        sbuf.append(strPattern, handledPosition, delimIndex - 1)
                        sbuf.append(Convert.utf8Str(argArray[argIndex]))
                        delimIndex + 2
                    } else {
                        // 占位符被转义
                        argIndex--
                        sbuf.append(strPattern, handledPosition, delimIndex - 1)
                        sbuf.append(C_DELIM_START)
                        delimIndex + 1
                    }
                } else {
                    // 正常占位符
                    sbuf.append(strPattern, handledPosition, delimIndex)
                    sbuf.append(Convert.utf8Str(argArray[argIndex]))
                    delimIndex + 2
                }
            }
            argIndex++
        }
        // 加入最后一个占位符后所有的字符
        sbuf.append(strPattern, handledPosition, strPattern.length)
        return sbuf.toString()
    }
}