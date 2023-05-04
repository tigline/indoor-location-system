package com.lgguan.iot.position.service

import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.fasterxml.jackson.core.type.TypeReference
import com.lgguan.iot.position.bean.*
import com.lgguan.iot.position.entity.Model
import com.lgguan.iot.position.entity.ModelProperties
import com.lgguan.iot.position.mapper.IModelMapper
import com.lgguan.iot.position.util.objectMapper
import java.util.*


interface IModelService: IService<Model> {
    fun addModel(addModel: AddOrUpdateModel): RestValue<Boolean>
    fun updateModel(modelId: String, updateModel: AddOrUpdateModel): RestValue<Boolean>
    fun deleteModel(modelId: String): RestValue<Boolean>
    fun updateModelActive(modelId: String, active: Boolean): RestValue<Boolean>
}


class ModelService: IModelService, ServiceImpl<IModelMapper, Model>() {

    override fun addModel(addModel: AddOrUpdateModel): RestValue<Boolean> {
        val model = Model().apply {
            modelName = addModel.modelName
            modelVersion = addModel.modelVersion
            companyId = addModel.companyId
            properties = objectMapper.readValue(addModel.properties, object: TypeReference<List<ModelProperties>>() {})
            configs = objectMapper.readValue(addModel.configs, object: TypeReference<List<ModelProperties>>() {})
            events = objectMapper.readValue(addModel.events, object: TypeReference<List<ModelProperties>>() {})
            commands = objectMapper.readValue(addModel.commands, object: TypeReference<List<ModelProperties>>() {})
            active = true
            createTime = Date()
        }
        return okOf(this.save(model))
    }

    override fun updateModel(modelId: String, updateModel: AddOrUpdateModel): RestValue<Boolean> {
        val model = this.getById(modelId);
        model ?: return failedOf(IErrorCode.DataNotExists, "ModelId [$modelId] not exists")
        model.apply {
            model.modelName = updateModel.modelName
            model.modelVersion = updateModel.modelVersion
            model.companyId = updateModel.companyId
            model.properties = objectMapper.readValue(updateModel.properties, object: TypeReference<List<ModelProperties>>() {})
            model.configs = objectMapper.readValue(updateModel.configs, object: TypeReference<List<ModelProperties>>() {})
            model.events = objectMapper.readValue(updateModel.events, object: TypeReference<List<ModelProperties>>() {})
            model.commands = objectMapper.readValue(updateModel.commands, object: TypeReference<List<ModelProperties>>() {})
            model.updateTime = Date()
        }
        return okOf(this.updateById(model))
    }

    override fun deleteModel(modelId: String): RestValue<Boolean> {
        val model = this.getById(modelId);
        model ?: return failedOf(IErrorCode.DataNotExists, "ModelId [$modelId] not exists")
        val res = this.removeById(modelId)
        return okOf(res)
    }

    override fun updateModelActive(modelId: String, active: Boolean): RestValue<Boolean> {
        val model = this.getById(modelId);
        model ?: return failedOf(IErrorCode.DataNotExists, "ModelId [$modelId] not exists")
        model.apply {
            model.active = active
            updateTime = Date()
        }
        return okOf(this.updateById(model))
    }

}