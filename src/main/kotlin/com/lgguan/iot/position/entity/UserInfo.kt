package com.lgguan.iot.position.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.lgguan.iot.position.bean.RoleType

/**
 *
 *
 * @author N.Liu
 **/
@TableName(autoResultMap = true)
class UserInfo {
    @TableId(type = IdType.ASSIGN_ID)
    var userId: String? = null
    var username: String? = null
    var password: String? = null
    var nickname: String? = null
    var email: String? = null
    var phone: String? = null
    var role: RoleType? = null
}
