package com.lgguan.iot.position.bean

class DepartmentTree {
    var depId: Long? = null
    var name: String? = null
    var parentId: Long? = null
    var children: MutableList<DepartmentTree>? = null
}
