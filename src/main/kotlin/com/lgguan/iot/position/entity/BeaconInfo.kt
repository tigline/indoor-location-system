package com.lgguan.iot.position.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.baomidou.mybatisplus.extension.activerecord.Model
import com.lgguan.iot.position.bean.BeaconType
import com.lgguan.iot.position.bean.BindStatus


/**
 *
 *
 * @author N.Liu
 **/
@TableName(autoResultMap = true)
class BeaconInfo : Model<BeaconInfo>() {
    @TableId(type = IdType.INPUT)
    var deviceId: String? = null
    var mac: String? = null
    var gateway: String? = null
    var mapId: String? = null
    var groupId: String? = null
    var name: String? = null
    var productName: String? = null
    var systemId: String? = null
    var type: BeaconType? = null
    var status: BindStatus? = null
    var online: Boolean? = false
    var fenceIds: String? = null
    var motion: String? = null
    var optScale: Float? = null
    var positionType: String? = null
    var posX: Float? = null
    var posY: Float? = null
    var updateTime: Long? = null
    var extraInfo: String? = null
}
