package com.lgguan.iot.position.service

import arrow.core.flatMap
import cn.hutool.core.date.DateUtil
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.lgguan.iot.position.bean.*
import com.lgguan.iot.position.entity.AlarmInfo
import com.lgguan.iot.position.ext.convert
import org.springframework.stereotype.Service

@Service
class AlarmManageService(val alarmInfoService: IAlarmInfoService) {
    fun pageAlarmInfos(param: QueryAlarmParam, pageLimit: PageLimit): PageResult<AlarmInfo> {
        return alarmInfoService.page(
            pageLimit.convert(), KtQueryWrapper(AlarmInfo::class.java)
                .eq(param.type != null, AlarmInfo::type, param.type)
                .eq(param.status != null, AlarmInfo::status, param.status)
                .between(
                    param.startTime != null && param.endTime != null,
                    AlarmInfo::createTime, param.startTime, param.endTime
                )
                .like(!param.deviceId.isNullOrEmpty(), AlarmInfo::deviceId, param.deviceId)
                .orderByDesc(AlarmInfo::createTime)
        ).convert()
    }

    fun dealWithAlarm(alarmId: Long, status: AlarmStatus): RestValue<Boolean> {
        val alarmInfo = alarmInfoService.getById(alarmId)
        alarmInfo ?: return failedOf(IErrorCode.DataNotExists, "AlarmId [$alarmId] not exists")
        alarmInfo.apply {
            this.status = status
            updateTime = DateUtil.currentSeconds()
        }
        val update = alarmInfoService.updateById(alarmInfo)
        return okOf(update)
    }

    fun deleteAlarmByIds(alarmIds: Array<Long>): Boolean {
        return alarmInfoService.removeByIds(alarmIds.toList())
    }

    fun getAlarmSummary(): AlarmSummary {
        val date = DateUtil.date()
        val startTime = DateUtil.beginOfDay(date).time / 1000
        val endTime = DateUtil.endOfDay(date).time / 1000
        val alarmInfos = alarmInfoService.list(
            KtQueryWrapper(AlarmInfo::class.java)
                .between(AlarmInfo::createTime, startTime, endTime)
        )

        val total = alarmInfos.size
        val statusMap = alarmInfos.groupBy { it.status }
            .flatMap {
                mapOf(it.key to it.value.size)
            }.let {
                val statusMap = mutableMapOf<AlarmStatus, Int>()
                AlarmStatus.values().forEach { alarmStatus ->
                    statusMap[alarmStatus] = it[alarmStatus] ?: 0
                }
                statusMap
            }

        val alarmRanks = alarmInfos.groupBy { it.deviceId }
            .map {
                it.key to it.value.size
            }.sortedBy {
                it.second
            }.reversed()
            .mapIndexed { index, pair ->
                AlarmRank(index + 1, pair.first!!, pair.second)
            }.take(5)

        val alarmRatio = alarmInfos.groupBy { it.type }
            .flatMap {
                mapOf(it.key to it.value.size)
            }.let {
                val alarmRatio = mutableMapOf<AlarmType, Int>()
                AlarmType.values().forEach { alarmType ->
                    alarmRatio[alarmType] = it[alarmType] ?: 0
                }
                alarmRatio
            }

        return AlarmSummary(total, statusMap, alarmRanks, alarmRatio)
    }
}
