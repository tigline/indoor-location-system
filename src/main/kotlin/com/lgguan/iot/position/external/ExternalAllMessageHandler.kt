package com.lgguan.iot.position.external

import cn.hutool.json.JSONUtil
import com.fasterxml.jackson.core.type.TypeReference
import com.lgguan.iot.position.bean.IotBeaconData
import com.lgguan.iot.position.bean.IotStationData
import com.lgguan.iot.position.util.objectMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ExternalAllMessageHandler(val externalFenceHandler: ExternalFenceHandler) {
    private val log = LoggerFactory.getLogger(javaClass)

    val TYPE_LOCATORS = "locators" //基站
    val TYPE_SENSORS = "sensors" //信标

    fun handler(payload: String, topic: String, companyCode: String, type: String){
        when (type) {

            TYPE_LOCATORS -> {
                val stationData: List<IotStationData> = objectMapper.readValue(payload, object : TypeReference<List<IotStationData>>() {})
                log.info("station data: "+JSONUtil.toJsonStr(stationData))
            }

            TYPE_SENSORS -> {
                val beaconData: List<IotBeaconData> = objectMapper.readValue(payload, object : TypeReference<List<IotBeaconData>>() {})
                log.info("beacon data: "+JSONUtil.toJsonStr(beaconData))


            }

            else -> {
                log.warn("Unsupported topic: [$topic], companyCode: [$companyCode], type: [$type]")
            }
        }

    }

}