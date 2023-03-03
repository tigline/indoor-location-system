package com.lgguan.iot.position.ext

import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.lgguan.iot.position.bean.PageLimit
import com.lgguan.iot.position.bean.PageResult

/**
 *
 *
 * @author N.Liu
 **/

fun <T> IPage<T>.convert(): PageResult<T> {
    return PageResult(current, size, total, records)
}

fun <T> PageLimit.convert(): IPage<T> {
    return Page(current, size)
}
