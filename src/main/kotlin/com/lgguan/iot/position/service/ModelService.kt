package com.lgguan.iot.position.service

import cn.hutool.json.JSONUtil
import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.lgguan.iot.position.bean.*
import com.lgguan.iot.position.entity.Model
import com.lgguan.iot.position.mapper.IModelMapper
import org.springframework.stereotype.Service
import java.util.*


interface IModelService: IService<Model> {
    fun addModel(addModel: AddOrUpdateModel): RestValue<Boolean>
    fun updateModel(modelId: Int, updateModel: AddOrUpdateModel): RestValue<Boolean>
    fun deleteModel(modelId: Int): RestValue<Boolean>
    fun updateModelActive(modelId: Int, active: Boolean): RestValue<Boolean>
    fun getAllModelInfo(): RestValue<List<IotModelResponse>>
}

@Service
class ModelService: IModelService, ServiceImpl<IModelMapper, Model>() {

    override fun addModel(addModel: AddOrUpdateModel): RestValue<Boolean> {
        val model = Model().apply {
            modelCode = addModel.modelCode
            modelName = addModel.modelName
            modelVersion = addModel.modelVersion
            companyId = addModel.companyId
            properties = JSONUtil.parseArray(addModel.properties)
            configs = JSONUtil.parseArray(addModel.configs)
            events = JSONUtil.parseArray(addModel.events)
            commands = JSONUtil.parseArray(addModel.commands)
            active = true
            createTime = Date()
        }
        return okOf(this.save(model))
    }

    override fun updateModel(modelId: Int, updateModel: AddOrUpdateModel): RestValue<Boolean> {
        val model = this.getById(modelId)
        model ?: return failedOf(IErrorCode.DataNotExists, "ModelId [$modelId] not exists")
        model.apply {
            model.modelCode = updateModel.modelCode
            model.modelName = updateModel.modelName
            model.modelVersion = updateModel.modelVersion
            model.companyId = updateModel.companyId
            model.properties = JSONUtil.parseArray(updateModel.properties)
            model.configs = JSONUtil.parseArray(updateModel.configs)
            model.events = JSONUtil.parseArray(updateModel.events)
            model.commands = JSONUtil.parseArray(updateModel.commands)
            model.updateTime = Date()
        }
        return okOf(this.updateById(model))
    }

    override fun deleteModel(modelId: Int): RestValue<Boolean> {
        val model = this.getById(modelId);
        model ?: return failedOf(IErrorCode.DataNotExists, "ModelId [$modelId] not exists")
        val res = this.removeById(modelId)
        return okOf(res)
    }

    override fun updateModelActive(modelId: Int, active: Boolean): RestValue<Boolean> {
        val model = this.getById(modelId);
        model ?: return failedOf(IErrorCode.DataNotExists, "ModelId [$modelId] not exists")
        model.apply {
            model.active = active
            updateTime = Date()
        }
        return okOf(this.updateById(model))
    }

    override fun getAllModelInfo(): RestValue<List<IotModelResponse>> {
        return okOf(this.getBaseMapper().getAllModelInfo())
    }

}