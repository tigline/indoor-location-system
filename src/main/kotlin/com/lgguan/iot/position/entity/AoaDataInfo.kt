package com.lgguan.iot.position.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.baomidou.mybatisplus.extension.activerecord.Model
import com.lgguan.iot.position.bean.BeaconType

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
    var type: BeaconType? = null
    var mapId: String? = null
    var optScale: Float? = null
    var posX: Float? = null
    var posY: Float? = null
    var timestamp: Long? = null
}
