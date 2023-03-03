package com.lgguan.iot.position.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.baomidou.mybatisplus.extension.activerecord.Model
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler
import com.lgguan.iot.position.bean.AlarmStatus
import com.lgguan.iot.position.bean.AlarmType
import com.lgguan.iot.position.util.Point

@TableName(autoResultMap = true)
class AlarmInfo : Model<AlarmInfo>() {
    @TableId(type = IdType.AUTO)
    var alarmId: Long? = null
    var deviceId: String? = null
    var name: String? = null
    var mapId: String? = null
    var fenceId: String? = null
    var type: AlarmType? = null
    var content: String? = null
    var status: AlarmStatus? = null

    @TableField(typeHandler = JacksonTypeHandler::class)
    var point: Point? = null
    var updateTime: Long? = null
    var createTime: Long? = null
}
