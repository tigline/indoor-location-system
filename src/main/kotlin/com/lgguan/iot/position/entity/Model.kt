package com.lgguan.iot.position.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler
import java.util.*

@TableName(value = "t_model")
class Model {
    @TableId(type = IdType.AUTO)
    var modelId: String? = null
    var modelName: String? = null
    var modelVersion: String? = null
    var companyId: Int? = null
    @TableField(value = "properties", typeHandler = JacksonTypeHandler::class)
    var properties: List<ModelProperties>? = null
    @TableField(value = "configs", typeHandler = JacksonTypeHandler::class)
    var configs: List<ModelProperties>? = null
    @TableField(value = "events", typeHandler = JacksonTypeHandler::class)
    var events: List<ModelProperties>? = null
    @TableField(value = "commands", typeHandler = JacksonTypeHandler::class)
    var commands: List<ModelProperties>? = null
    var active: Boolean? = false
    var createTime: Date? = null
    var updateTime: Date? = null
}