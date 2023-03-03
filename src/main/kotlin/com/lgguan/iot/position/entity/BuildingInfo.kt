package com.lgguan.iot.position.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.extension.activerecord.Model

/**
 *
 *
 * @author N.Liu
 **/
class BuildingInfo: Model<BuildingInfo>() {
    @TableId(type = IdType.ASSIGN_ID)
    var buildingId: String? = null
    var name: String? = null
    var address: String? = null
    var picture: String? = null
}
