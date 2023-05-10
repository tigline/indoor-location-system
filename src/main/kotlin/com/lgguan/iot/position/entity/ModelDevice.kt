package com.lgguan.iot.position.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.util.*

@TableName(value = "t_model_device")
class ModelDevice {
    @TableId(type = IdType.AUTO)
    var id: String? = null
    var deviceId: String? = null
    var clientId: String? = null
    var companyId: Int? = null
    var modelId: Int? = null
    var active: Boolean? = false
    var createTime: Date? = null
    var updateTime: Date? = null
}