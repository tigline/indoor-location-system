package com.lgguan.iot.position.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.extension.activerecord.Model

class DepartmentInfo : Model<DepartmentInfo>() {
    @TableId(type = IdType.AUTO)
    var depId: Long? = null
    var name: String? = null
    var parentId: Long? = 0
}
