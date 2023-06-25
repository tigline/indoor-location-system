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
class IotBeaconData : Serializable {

    var gateway: String? = null
    var nodeId: String? = null
    var systemId: String? = null
    var type: String? = null
    var mac: String? = null
    var motion: String? = null
    var optScale: Double? = null
    var positionType: String? = null
    var posX: Double? = null
    var posY: Double? = null
    var posZ: Double? = null
    var time: Long? = null
    var mapId: String? = null
    var zoneId: String? = null
    var beaconId: String? = null
}