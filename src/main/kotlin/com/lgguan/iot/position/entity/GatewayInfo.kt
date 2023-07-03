package com.lgguan.iot.position.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.baomidou.mybatisplus.extension.activerecord.Model
import com.lgguan.iot.position.bean.GatewayType
import com.lgguan.iot.position.bean.OnlineStatus

/**
 *
 *
 * @author N.Liu
 **/
@TableName(autoResultMap = true)
class GatewayInfo: Model<GatewayInfo>() {
    @TableId(type = IdType.INPUT)
    var gateway: String? = null
    var mapId: String? = null
    var groupId: String? = null
    var name: String? = null
    var productName: String? = null
    var systemId: String? = null
    var type: GatewayType? = null
    var status: OnlineStatus? = null
    var ip: String? = null
    var fenceIds: String? = null
    var setX: Double? = null
    var setY: Double? = null
    var setZ: Double? = null
    var angle: Double? = null
    var hisX: Double? = null
    var hisY: Double? = null
    var hisZ: Double? = null
    var updateTime: Long? = null
    var extraInfo: String? = null
    var zoneId: String? = null
    var companyCode: String? = null
}
