package com.lgguan.iot.position.external

import cn.hutool.core.date.DateUtil
import com.lgguan.iot.position.bean.*
import com.lgguan.iot.position.entity.AlarmInfo
import com.lgguan.iot.position.entity.BeaconInfo
import com.lgguan.iot.position.entity.FenceInfo
import com.lgguan.iot.position.service.IBeaconInfoService
import com.lgguan.iot.position.service.IFenceInfoService
import com.lgguan.iot.position.util.Point
import com.lgguan.iot.position.util.fenceListCache
import com.lgguan.iot.position.util.inRange
import com.lgguan.iot.position.ws.sendWsMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component

@Component
class ExternalFenceHandler(val fenceInfoService: IFenceInfoService, val beaconInfoService: IBeaconInfoService) {
    val aoaDataFlow = MutableSharedFlow<BeaconInfo>()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            aoaDataFlow.collect { beaconInfo ->
                val fenceInfos = fenceListCache.get(beaconInfo.mapId) { mapId ->
                    fenceInfoService.ktQuery()
                        .eq(BeaconInfo::mapId, mapId)
                        .eq(FenceInfo::enabled, true)
                        .list() ?: listOf()
                }
                val fenceIds = beaconInfo.fenceIds?.split(",")?.toList() ?: listOf()
                val name = beaconInfoService.getBoundNameByDeviceId(beaconInfo.deviceId!!) ?: ""
                val currentPoint = Point(beaconInfo.posX!!, beaconInfo.posY!!)
                var prevPoint = beaconInfo.prevPoint ?: Point(0f, 0f)
                val currentTime = DateUtil.currentSeconds()
                fenceInfos.forEach { fenceInfo ->
                    val inRange = currentPoint inRange fenceInfo.points
                    when (fenceInfo.type) {
                        FenceType.In -> {
                            if (inRange && !fenceIds.contains(fenceInfo.fenceId)) {
                                val prevRange = prevPoint inRange fenceInfo.points
                                if (!prevRange) {
                                    val alarmInfo = AlarmInfo().apply {
                                        deviceId = beaconInfo.deviceId
                                        this.name = name
                                        mapId = fenceInfo.mapId
                                        fenceId = fenceInfo.fenceId
                                        type = AlarmType.In
                                        content = "非法闯入"
                                        status = AlarmStatus.Unprocessed
                                        point = currentPoint
                                        updateTime = currentTime
                                        createTime = currentTime
                                    }
                                    alarmInfo.insert()
                                    sendWsMessage(WsMessage(MessageType.Alarm, alarmInfo))
                                }
                            }
                        }

                        FenceType.Out -> {
                            if (!inRange && fenceIds.contains(fenceInfo.fenceId)) {
                                val prevRange = prevPoint inRange fenceInfo.points
                                if (prevRange) {
                                    val alarmInfo = AlarmInfo().apply {
                                        deviceId = beaconInfo.deviceId
                                        this.name = name
                                        mapId = fenceInfo.mapId
                                        fenceId = fenceInfo.fenceId
                                        type = AlarmType.Out
                                        content = "非法越界"
                                        status = AlarmStatus.Unprocessed
                                        point = currentPoint
                                        updateTime = currentTime
                                        createTime = currentTime
                                    }
                                    alarmInfo.insert()
                                    sendWsMessage(WsMessage(MessageType.Alarm, alarmInfo))
                                }
                            }
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    suspend fun emit(pair: BeaconInfo) {
        aoaDataFlow.emit(pair)
    }
}
