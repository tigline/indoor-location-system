package com.lgguan.iot.position.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.baomidou.mybatisplus.extension.activerecord.Model

@TableName(autoResultMap = true)
class MapInfo: Model<MapInfo>() {
    @TableId(type = IdType.ASSIGN_ID)
    var mapId: String? = null
    var name: String? = null
    var buildingId: String? = null
    var floor: String? = null
    var width: Float? = null
    var length: Float? = null
    var widthPx: Float? = null
    var lengthPx: Float? = null
    var picture: String? = null
    var companyCode: String? = null
    var coordinateType: CoordinateType? = null
}

enum class CoordinateType {
    Cartesian, World
}
