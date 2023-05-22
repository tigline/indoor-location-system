package com.lgguan.iot.position.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.util.*

@TableName(value = "t_command_record")
class CommandRecord {
    @TableId(type = IdType.AUTO)
    var id: Int? = null
    var alias: String? = null
    var templateId: Int? = null
    var content: String? = null
    var param: String? = null
    var immediately: Int? = null
    var planExecuteTime: Date? = null
    var timeout: Int? = null
    var description: String? = null
    var status: Int? = null
    var executeEndTime: Date? = null
    var creator: String? = null
    var createTime: Date? = null
    var updater: String? = null
    var updateTime: Date? = null
}