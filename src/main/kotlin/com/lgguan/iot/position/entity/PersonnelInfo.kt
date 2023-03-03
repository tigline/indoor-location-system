package com.lgguan.iot.position.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.lgguan.iot.position.bean.SexType

/**
 *
 *
 * @author N.Liu
 **/
class PersonnelInfo {
    @TableId(type = IdType.AUTO)
    var personnelId: Long? = null
    var name: String? = null
    var sex: SexType? = SexType.Male
    var tag: String? = null
    var typeId: Long? = null
    var depId: Long? = null
    var avatar: String? = null
    var createTime: Long? = null
}
