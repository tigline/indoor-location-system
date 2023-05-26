package com.lgguan.iot.position.util.file

import org.apache.commons.lang3.time.DateFormatUtils
import org.apache.commons.lang3.time.DateUtils
import java.lang.management.ManagementFactory
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.*

/**
 * 时间工具类
 *
 */
object DateUtils : DateUtils() {
    var YYYY = "yyyy"
    var YYYY_MM = "yyyy-MM"
    var YYYY_MM_DD = "yyyy-MM-dd"
    var YYYYMMDDHHMMSS = "yyyyMMddHHmmss"
    var YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss"
    private val parsePatterns = arrayOf(
        "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
        "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
        "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"
    )

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    val nowDate: Date
        get() = Date()

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    val date: String
        get() = dateTimeNow(YYYY_MM_DD)
    val time: String
        get() = dateTimeNow(YYYY_MM_DD_HH_MM_SS)

    @JvmOverloads
    fun dateTimeNow(format: String? = YYYYMMDDHHMMSS): String {
        return parseDateToStr(format, Date())
    }

    fun dateTime(date: Date?): String {
        return parseDateToStr(YYYY_MM_DD, date)
    }

    fun parseDateToStr(format: String?, date: Date?): String {
        return SimpleDateFormat(format).format(date)
    }

    fun dateTime(format: String?, ts: String?): Date {
        return try {
            SimpleDateFormat(format).parse(ts)
        } catch (e: ParseException) {
            throw RuntimeException(e)
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    fun datePath(): String {
        val now = Date()
        return DateFormatUtils.format(now, "yyyy/MM/dd")
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    fun dateTime(): String {
        val now = Date()
        return DateFormatUtils.format(now, "yyyyMMdd")
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    fun parseDate(str: Any?): Date? {
        return if (str == null) {
            null
        } else try {
            parseDate(str.toString(), *parsePatterns)
        } catch (e: ParseException) {
            null
        }
    }

    /**
     * 获取服务器启动时间
     */
    val serverStartDate: Date
        get() {
            val time = ManagementFactory.getRuntimeMXBean().startTime
            return Date(time)
        }

    /**
     * 计算时间差
     *
     * @param endTime 最后时间
     * @param startTime 开始时间
     * @return 时间差（天/小时/分钟）
     */
    fun timeDistance(endDate: Date, startTime: Date): String {
        val nd = (1000 * 24 * 60 * 60).toLong()
        val nh = (1000 * 60 * 60).toLong()
        val nm = (1000 * 60).toLong()
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        val diff = endDate.time - startTime.time
        // 计算差多少天
        val day = diff / nd
        // 计算差多少小时
        val hour = diff % nd / nh
        // 计算差多少分钟
        val min = diff % nd % nh / nm
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day.toString() + "天" + hour + "小时" + min + "分钟"
    }

    /**
     * 增加 LocalDateTime ==> Date
     */
    fun toDate(temporalAccessor: LocalDateTime): Date {
        val zdt = temporalAccessor.atZone(ZoneId.systemDefault())
        return Date.from(zdt.toInstant())
    }

    /**
     * 增加 LocalDate ==> Date
     */
    fun toDate(temporalAccessor: LocalDate?): Date {
        val localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0))
        val zdt = localDateTime.atZone(ZoneId.systemDefault())
        return Date.from(zdt.toInstant())
    }
}