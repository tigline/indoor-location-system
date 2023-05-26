package com.lgguan.iot.position.util.file

import com.lgguan.iot.position.util.file.DateUtils.dateTimeNow
import java.util.concurrent.atomic.AtomicInteger

/**
 * 序列生成类
 */
object Seq {
    // 通用序列类型
    const val commSeqType = "COMMON"

    // 上传序列类型
    const val uploadSeqType = "UPLOAD"

    // 通用接口序列数
    private val commSeq = AtomicInteger(1)

    // 上传接口序列数
    private val uploadSeq = AtomicInteger(1)

    // 机器标识
    private const val machineCode = "A"

    /**
     * 获取通用序列号
     *
     * @return 序列值
     */
    val id: String?
        get() = getId(commSeqType)

    /**
     * 默认16位序列号 yyMMddHHmmss + 一位机器标识 + 3长度循环递增字符串
     *
     * @return 序列值
     */
    fun getId(type: String?): String? {
        var atomicInt = commSeq
        if (uploadSeqType == type) {
            atomicInt = uploadSeq
        }
        return getId(atomicInt, 3)
    }

    /**
     * 通用接口序列号 yyMMddHHmmss + 一位机器标识 + length长度循环递增字符串
     *
     * @param atomicInt 序列数
     * @param length 数值长度
     * @return 序列值
     */
    fun getId(atomicInt: AtomicInteger, length: Int): String? {
        var result: String? = dateTimeNow()
        result += machineCode
        result += getSeq(atomicInt, length)
        return result
    }

    /**
     * 序列循环递增字符串[1, 10 的 (length)幂次方), 用0左补齐length位数
     *
     * @return 序列值
     */
    @Synchronized
    private fun getSeq(atomicInt: AtomicInteger, length: Int): String? {
        // 先取值再+1
        val value = atomicInt.getAndIncrement()

        // 如果更新后值>=10 的 (length)幂次方则重置为1
        val maxSeq = Math.pow(10.0, length.toDouble()).toInt()
        if (atomicInt.get() >= maxSeq) {
            atomicInt.set(1)
        }
        // 转字符串，用0左补齐
        return StringUtils.padl(value, length)
    }
}