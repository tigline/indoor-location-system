package com.lgguan.iot.position.bean

import cn.hutool.json.JSONArray
import java.io.Serializable

class IotModelResponse: Serializable {
    var modelCode: String? = null
    var modelName: String? = null
    var versionName: String? = null
    var versionCode: Int? = null
    var companyCode: String? = null
    var properties: JSONArray? = null
    var configs: JSONArray? = null
    var events: JSONArray? = null
    var commands: JSONArray? = null
}