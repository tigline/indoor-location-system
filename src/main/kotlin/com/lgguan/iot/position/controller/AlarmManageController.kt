package com.lgguan.iot.position.controller

import com.lgguan.iot.position.bean.*
import com.lgguan.iot.position.entity.AlarmInfo
import com.lgguan.iot.position.service.AlarmManageService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.core.annotations.ParameterObject
import org.springframework.web.bind.annotation.*

@Tag(name = "告警管理")
@RestController
@RequestMapping("/api/v1")
class AlarmManageController(val alarmManageService: AlarmManageService) {

    @Operation(summary = "分页获取告警信息")
    @GetMapping("/alarms")
    fun pageAlarm(
        @ParameterObject param: QueryAlarmParam,
        @ParameterObject pageLimit: PageLimit
    ): RestValue<PageResult<AlarmInfo>> {
        val res = alarmManageService.pageAlarmInfos(param, pageLimit)
        return okOf(res)
    }

    @Operation(summary = "告警处理")
    @PutMapping("/alarms/{alarmId}/{status}")
    fun dealWithAlarm(@PathVariable alarmId: Long, @PathVariable status: AlarmStatus): RestValue<Boolean> {
        return alarmManageService.dealWithAlarm(alarmId, status)
    }

    @Operation(summary = "(批量)删除告警")
    @DeleteMapping("/alarms")
    fun deleteAlarm(
        @Parameter(
            name = "alarmIds",
            required = true,
            `in` = ParameterIn.QUERY,
            description = "需要删除的id数组，以逗号分隔"
        ) alarmIds: Array<Long>
    ): RestValue<Boolean> {
        if (alarmIds.isEmpty()) {
            return failedOf(IErrorCode.ParameterMissing, "AlarmIds is empty")
        }
        val res = alarmManageService.deleteAlarmByIds(alarmIds)
        return okOf(res)
    }

    @Operation(summary = "获取今日告警统计信息")
    @GetMapping("/alarm/summary")
    fun getAlarmSummary():RestValue<AlarmSummary> {
        val res = alarmManageService.getAlarmSummary()
        return okOf(res)
    }
}
