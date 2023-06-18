package com.lgguan.iot.position.bean

import cn.hutool.json.JSONArray
import java.io.Serializable

class IotModelResponse: Serializable {
    var modelCode: String? = null
    var jsonType: Int? = null
    var versionName: String? = null
    var versionCode: Int? = null
    var companyCode: String? = null
    var topics: JSONArray? = null
    var active: Boolean? = false
}