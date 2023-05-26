package com.lgguan.iot.position.exception

open class BaseException @JvmOverloads constructor(
    /**
     * 所属模块
     */
    val module: String?,
    /**
     * 错误码
     */
    var code: String?, args: Array<Any>?, defaultMessage: String? = null
) :
    RuntimeException() {

    /**
     * 错误码对应的参数
     */
    val args: Array<Any>?

    /**
     * 错误消息
     */
    val defaultMessage: String?

    init {
        this.code = code
        this.args = args
        this.defaultMessage = defaultMessage
    }

    constructor(module: String?, defaultMessage: String?) : this(module, null, null, defaultMessage) {}
    constructor(code: String?, args: Array<Any>?) : this(null, code, args, null) {}
    constructor(defaultMessage: String?) : this(null, null, null, defaultMessage) {}

    companion object {
        private const val serialVersionUID = 1L
    }
}