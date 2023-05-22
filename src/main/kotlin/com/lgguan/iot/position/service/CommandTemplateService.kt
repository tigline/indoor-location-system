package com.lgguan.iot.position.service

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.lgguan.iot.position.bean.*
import com.lgguan.iot.position.entity.CommandTemplate
import com.lgguan.iot.position.mapper.ICommandTemplateMapper
import java.util.*

interface ICommandTemplateService: IService<CommandTemplate> {
    fun listCommandTemplate(name: String?, title: String?, companyId: Int?): List<CommandTemplate>
    fun addCommandTemplate(addCommandTemplate: AddOrUpdateCommandTemplate): RestValue<Boolean>
    fun updateCommandTemplate(id: Int, updateCommandTemplate: AddOrUpdateCommandTemplate): RestValue<Boolean>
    fun deleteCommandTemplate(id: Int): RestValue<Boolean>
    fun updateCommandTemplateActive(id: Int, active: Boolean): RestValue<Boolean>
}

class CommandTemplateService: ICommandTemplateService, ServiceImpl<ICommandTemplateMapper, CommandTemplate>(){
    override fun listCommandTemplate(name: String?, title: String?, companyId: Int?): List<CommandTemplate> {
        return this.list(
            KtQueryWrapper(CommandTemplate::class.java)
                .like(!name.isNullOrEmpty(), CommandTemplate::name, name)
                .like(!title.isNullOrEmpty(), CommandTemplate::title, title)
                .eq(companyId != null, CommandTemplate::companyId, companyId)
        )
    }

    override fun addCommandTemplate(addCommandTemplate: AddOrUpdateCommandTemplate): RestValue<Boolean> {
        val commandTemplate = CommandTemplate().apply {
            name = addCommandTemplate.name
            title = addCommandTemplate.title
            companyId = addCommandTemplate.companyId
            modelId = addCommandTemplate.modelId
            content = addCommandTemplate.content
            param = addCommandTemplate.param
            ackCheck = addCommandTemplate.ackCheck
            enableCheck = addCommandTemplate.enableCheck
            category = addCommandTemplate.category
            onVersion = addCommandTemplate.onVersion
            description = addCommandTemplate.description
            active = true
            createTime = Date()
        }
        return okOf(this.save(commandTemplate))
    }

    override fun updateCommandTemplate(id: Int, updateCommandTemplate: AddOrUpdateCommandTemplate): RestValue<Boolean> {
        val commandTemplate = this.getById(id)
        commandTemplate ?: return failedOf(IErrorCode.DataNotExists, "CommandTemplate [$id] not exists")
        commandTemplate.apply {
            name = updateCommandTemplate.name
            title = updateCommandTemplate.title
            companyId = updateCommandTemplate.companyId
            modelId = updateCommandTemplate.modelId
            content = updateCommandTemplate.content
            param = updateCommandTemplate.param
            ackCheck = updateCommandTemplate.ackCheck
            enableCheck = updateCommandTemplate.enableCheck
            category = updateCommandTemplate.category
            onVersion = updateCommandTemplate.onVersion
            description = updateCommandTemplate.description
            updateTime = Date()
        }
        return okOf(this.updateById(commandTemplate))
    }

    override fun deleteCommandTemplate(id: Int): RestValue<Boolean> {
        val commandTemplate = this.getById(id)
        commandTemplate ?: return failedOf(IErrorCode.DataNotExists, "CommandTemplateId [$id] not exists")
        val res = this.removeById(id)
        return okOf(res)
    }

    override fun updateCommandTemplateActive(id: Int, active: Boolean): RestValue<Boolean> {
        val commandTemplate = this.getById(id)
        commandTemplate ?: return failedOf(IErrorCode.DataNotExists, "CommandTemplateId [$id] not exists")
        commandTemplate.apply {
            commandTemplate.active = active
            updateTime = Date()
        }
        return okOf(this.updateById(commandTemplate))
    }

}