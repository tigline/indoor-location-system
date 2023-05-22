package com.lgguan.iot.position.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.util.*

@TableName(value = "t_command_template")
class CommandTemplate {
    @TableId(type = IdType.AUTO)
    var id: Int? = null
    var name: String? = null
    var title: String? = null
    var companyId: Int? = null
    var modelId: Int? = null
    var content: String? = null
    var param: String? = null
    var ackCheck: Boolean? = false
    var enableCheck: Boolean? = true
    var category: String? = null
    var onVersion: Int? = null
    var description: String? = null
    var active: Boolean? = true
    var createTime: Date? = null
    var updateTime: Date? = null

}