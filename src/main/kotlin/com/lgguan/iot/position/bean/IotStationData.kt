package com.lgguan.iot.position.bean

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import java.io.Serializable

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class IotStationData : Serializable {
    var ip: String? = null
    var mac: String? = null
    var gateway: String? = null
    var type: String? = null
    var name: String? = null
    var time: Long? = null
    var mapId: String? = null
    var zoneId: String? = null
    var hisX: Double? = null
    var hisY: Double? = null
    var hisZ: Double? = null
    var angles: String? = null
    var online: Int? = null


    fun realGateway():String? {
        return gateway?.replace(":", "")
    }

}