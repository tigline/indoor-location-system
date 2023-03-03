package com.lgguan.iot.position.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.extension.activerecord.Model

/**
 *
 *
 * @author N.Liu
 **/
class ThingTypeInfo: Model<ThingTypeInfo>() {
    @TableId(type = IdType.AUTO)
    var typeId: Long? = null
    var typeName: String? = null
    var picture: String? = null
    var createTime: Long? = null
}
