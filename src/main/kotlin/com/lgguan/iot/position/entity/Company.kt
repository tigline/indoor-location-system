package com.lgguan.iot.position.entity

import cn.hutool.json.JSONObject
import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.baomidou.mybatisplus.extension.activerecord.Model
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler
import java.util.*

@TableName(value = "t_company", autoResultMap = true)
class Company : Model<Company>() {
    @TableId(type = IdType.AUTO)
    var id: Int? = null
    var companyCode: String? = null
    @TableField(typeHandler = JacksonTypeHandler::class)
    var companyName: JSONObject? = null
    var simpleName: String? = null
    var contactName: String? = null
    var contactPhone: String? = null
    var beginCreateTime: Date? = null
    var active: Boolean? = false
    var createTime: Date? = null
    var updateTime: Date? = null
}