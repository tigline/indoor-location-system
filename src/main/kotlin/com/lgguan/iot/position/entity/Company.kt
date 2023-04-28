package com.lgguan.iot.position.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.util.*

@TableName(value = "t_company")
class Company {
    @TableId(type = IdType.AUTO)
    var companyId: String? = null
    var companyCode: String? = null
    var companyName: String? = null
    var simpleName: String? = null
    var contactName: String? = null
    var contactPhone: String? = null
    var beginCreateTime: Date? = null
    var active: Boolean? = false
    var createTime: Date? = null
    var updateTime: Date? = null
}