package com.lgguan.iot.position.service

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
}
