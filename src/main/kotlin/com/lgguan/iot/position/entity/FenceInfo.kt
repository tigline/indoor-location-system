package com.lgguan.iot.position.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.baomidou.mybatisplus.extension.activerecord.Model
import com.lgguan.iot.position.bean.FenceType
import com.lgguan.iot.position.mapper.handler.PointListTypeHandler
import com.lgguan.iot.position.util.Point

/**
 *
 *
 * @author N.Liu
 **/
@TableName(autoResultMap = true)
class FenceInfo : Model<FenceInfo>() {
    @TableId(type = IdType.ASSIGN_ID)
    var fenceId: String? = null
    var name: String? = null
    var mapId: String? = null
    var type: FenceType? = null
    @TableField(typeHandler = PointListTypeHandler::class)
    var points: List<Point> = mutableListOf()
    var enabled: Boolean = false
}
