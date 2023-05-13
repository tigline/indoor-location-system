package com.lgguan.iot.position.entity

import cn.hutool.json.JSONArray
import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler
import java.util.*

@TableName(value = "t_model", autoResultMap = true)
class Model {
    @TableId(type = IdType.AUTO)
    var id: Int? = null
    var modelCode: String? = null
    var modelName: String? = null
    var modelVersion: String? = null
    var companyId: Int? = null
    @TableField(value = "properties", typeHandler = JacksonTypeHandler::class)
    var properties: JSONArray? = null
    @TableField(value = "configs", typeHandler = JacksonTypeHandler::class)
    var configs: JSONArray? = null
    @TableField(value = "events", typeHandler = JacksonTypeHandler::class)
    var events: JSONArray? = null
    @TableField(value = "commands", typeHandler = JacksonTypeHandler::class)
    var commands: JSONArray? = null
    var active: Boolean? = false
    var createTime: Date? = null
    var updateTime: Date? = null
    @Transient
    var companyCode: String? = null
}