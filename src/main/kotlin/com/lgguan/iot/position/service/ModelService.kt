package com.lgguan.iot.position.service

import cn.hutool.json.JSONUtil
import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.lgguan.iot.position.bean.*
import com.lgguan.iot.position.entity.Model
import com.lgguan.iot.position.mapper.IModelMapper
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.util.*


interface IModelService: IService<Model> {
    fun addModel(addModel: AddOrUpdateModel): RestValue<Boolean>
    fun updateModel(modelId: Int, updateModel: AddOrUpdateModel): RestValue<Boolean>
    fun deleteModel(modelId: Int): RestValue<Boolean>
    fun updateModelActive(modelId: Int, active: Boolean): RestValue<Boolean>
    fun getAllModelInfo(): RestValue<List<IotModelResponse>>
    fun getModelInfoByModelId(modelId: Int): IotModelResponse
}

@Service
class ModelService(val applicationEventPublisher: ApplicationEventPublisher): IModelService, ServiceImpl<IModelMapper, Model>() {

    override fun addModel(addModel: AddOrUpdateModel): RestValue<Boolean> {
        val model = Model().apply {
            modelCode = addModel.modelCode
            modelName = addModel.modelName
            versionName = addModel.versionName
            versionCode = addModel.versionCode
            companyId = addModel.companyId
            properties = JSONUtil.parseArray(addModel.properties)
            configs = JSONUtil.parseArray(addModel.configs)
            events = JSONUtil.parseArray(addModel.events)
            commands = JSONUtil.parseArray(addModel.commands)
            active = true
            createTime = Date()
        }
        val res = this.save(model)
        if(res){
            applicationEventPublisher.publishEvent(model)
        }
        return okOf(res)
    }

    override fun updateModel(modelId: Int, updateModel: AddOrUpdateModel): RestValue<Boolean> {
        val model = this.getById(modelId)
        model ?: return failedOf(IErrorCode.DataNotExists, "ModelId [$modelId] not exists")
        model.apply {
            model.modelCode = updateModel.modelCode
            model.modelName = updateModel.modelName
            model.versionName = updateModel.versionName
            model.versionCode = updateModel.versionCode
            model.companyId = updateModel.companyId
            model.properties = JSONUtil.parseArray(updateModel.properties)
            model.configs = JSONUtil.parseArray(updateModel.configs)
            model.events = JSONUtil.parseArray(updateModel.events)
            model.commands = JSONUtil.parseArray(updateModel.commands)
            model.updateTime = Date()
        }
        val res = this.updateById(model)
        if(res){
            applicationEventPublisher.publishEvent(model)
        }
        return okOf(res)
    }

    override fun deleteModel(modelId: Int): RestValue<Boolean> {
        val model = this.getById(modelId);
        model ?: return failedOf(IErrorCode.DataNotExists, "ModelId [$modelId] not exists")
        val res = this.removeById(modelId)
        if(res){
            applicationEventPublisher.publishEvent(model)
        }
        return okOf(res)
    }

    override fun updateModelActive(modelId: Int, active: Boolean): RestValue<Boolean> {
        val model = this.getById(modelId);
        model ?: return failedOf(IErrorCode.DataNotExists, "ModelId [$modelId] not exists")
        model.apply {
            model.active = active
            updateTime = Date()
        }
        val res = this.updateById(model)
        if(res){
            applicationEventPublisher.publishEvent(model)
        }
        return okOf(res)
    }

    override fun getAllModelInfo(): RestValue<List<IotModelResponse>> {
        return okOf(this.getBaseMapper().getAllModelInfo())
    }

    override fun getModelInfoByModelId(modelId: Int): IotModelResponse {
        return this.getBaseMapper().getModelInfoByModelId(modelId)
    }

}