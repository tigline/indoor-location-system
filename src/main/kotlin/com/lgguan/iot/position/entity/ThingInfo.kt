package com.lgguan.iot.position.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.extension.activerecord.Model

/**
 *
 *
 * @author N.Liu
 **/
class ThingInfo: Model<ThingInfo>() {
    @TableId(type = IdType.AUTO)
    var thingId: Long? = null
    var name: String? = null
    var tag: String? = null
    var typeId: Long? = null
    var picture: String? = null
}
