package com.lgguan.iot.position.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.baomidou.mybatisplus.extension.activerecord.Model
import com.lgguan.iot.position.bean.BeaconType
import java.util.*

/**
 *
 *
 * @author N.Liu
 **/
@TableName(autoResultMap = true)
class AoaDataInfo: Model<AoaDataInfo>() {
    @TableId(type = IdType.AUTO)
    var id: Long? = null
    var deviceId: String? = null
    var gatewayId: String? = null
    var type: BeaconType? = null
    var mapId: String? = null
    var zoneId: String? = null
    var optScale: Float? = null
    var posX: Float? = null
    var posY: Float? = null
    var posZ: Float? = null
    var timestamp: Long? = null
    var createTime: Date? = null
    var status:Int? = null  //0:freezing 1:moving

    fun copyFrom(other: AoaDataInfo) {
        id = other.id
        deviceId = other.deviceId
        type = other.type
        mapId = other.mapId
        zoneId = other.mapId
        optScale = other.optScale
        posX = other.posX
        posY = other.posY
        timestamp = other.timestamp
    }
}
