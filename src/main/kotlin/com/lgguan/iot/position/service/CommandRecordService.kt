package com.lgguan.iot.position.service

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.lgguan.iot.position.bean.*
import com.lgguan.iot.position.entity.CommandRecord
import com.lgguan.iot.position.mapper.ICommandRecordMapper
import java.util.*

interface ICommandRecordService: IService<CommandRecord> {
    fun listCommandRecord(alias: String?, templateId: Int?): List<CommandRecord>
    fun addCommandRecord(addCommandRecord: AddOrUpdateCommandRecord): RestValue<Boolean>
    fun deleteCommandRecord(id: Int): RestValue<Boolean>
}


class CommandRecordService: ICommandRecordService, ServiceImpl<ICommandRecordMapper, CommandRecord>(){

    override fun listCommandRecord(alias: String?, templateId: Int?): List<CommandRecord> {
        return this.list(
            KtQueryWrapper(CommandRecord::class.java)
                .like(!alias.isNullOrEmpty(), CommandRecord::alias, alias)
                .eq(templateId != null, CommandRecord::templateId, templateId)
        )
    }

    override fun addCommandRecord(addCommandRecord: AddOrUpdateCommandRecord): RestValue<Boolean> {
        val commandRecord = CommandRecord().apply {
            alias = addCommandRecord.alias
            templateId = addCommandRecord.templateId
            content = addCommandRecord.content
            param = addCommandRecord.param
            immediately = addCommandRecord.immediately
            planExecuteTime = addCommandRecord.planExecuteTime
            timeout = addCommandRecord.timeout
            description = addCommandRecord.description
            status = 0
            creator = addCommandRecord.creator
            createTime = Date()
        }
        val res = this.save(commandRecord)
        if(res){
            //send mq to execute command

        }
        return okOf(res)
    }

    override fun deleteCommandRecord(id: Int): RestValue<Boolean> {
        val commandRecord = this.getById(id)
        commandRecord ?: return failedOf(IErrorCode.DataNotExists, "CommandRecordId [$id] not exists")
        val res = this.removeById(id)
        return okOf(res)
    }

}