package com.lgguan.iot.position.service

import cn.hutool.json.JSONUtil
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.lgguan.iot.position.bean.*
import com.lgguan.iot.position.entity.Model
import com.lgguan.iot.position.ext.convert
import com.lgguan.iot.position.mapper.IModelMapper
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.util.*


interface IModelService: IService<Model> {
    fun pageModels(vendor: String?, companyId: Int?, pageLimit: PageLimit): PageResult<Model>
    fun addModel(addModel: AddOrUpdateModel): RestValue<Boolean>
    fun updateModel(modelId: Int, updateModel: AddOrUpdateModel): RestValue<Boolean>
    fun deleteModel(modelId: Int): RestValue<Boolean>
    fun updateModelActive(modelId: Int, active: Boolean): RestValue<Boolean>
    fun getAllModelInfo(): RestValue<List<IotModelResponse>>
    fun getModelInfoByModelId(modelId: Int): IotModelResponse
}

@Service
class ModelService(val applicationEventPublisher: ApplicationEventPublisher): IModelService, ServiceImpl<IModelMapper, Model>() {
    override fun pageModels(vendor: String?, companyId: Int?, pageLimit: PageLimit): PageResult<Model> {
        return page(
            pageLimit.convert(), KtQueryWrapper(Model::class.java)
                .eq(companyId != null, Model::companyId, companyId)
                .like(!vendor.isNullOrEmpty(), Model::modelCode, vendor)
                .eq(Model::active, true)
        ).convert()
    }

    override fun addModel(addModel: AddOrUpdateModel): RestValue<Boolean> {
        val model = Model().apply {
            modelCode = addModel.vendor
            versionName = addModel.versionName
            versionCode = addModel.versionCode
            companyId = addModel.companyId
            jsonType = addModel.jsonType
            topics = JSONUtil.parseArray(addModel.topics)
            active = true
            createTime = Date()
        }

        val res = this.save(model)
        if(res){
//            applicationEventPublisher.publishEvent(model)
        }
        return okOf(res)
    }

    override fun updateModel(modelId: Int, updateModel: AddOrUpdateModel): RestValue<Boolean> {
        val model = this.getById(modelId)
        model ?: return failedOf(IErrorCode.DataNotExists, "ModelId [$modelId] not exists")
        model.apply {
            model.modelCode = updateModel.vendor
            model.versionName = updateModel.versionName
            model.versionCode = updateModel.versionCode
            model.companyId = updateModel.companyId
            model.topics = JSONUtil.parseArray(updateModel.topics)
            model.updateTime = Date()
        }
        val res = this.updateById(model)
        if(res){
//            applicationEventPublisher.publishEvent(model)
        }
        return okOf(res)
    }

    override fun deleteModel(modelId: Int): RestValue<Boolean> {
        val model = this.getById(modelId);
        model ?: return failedOf(IErrorCode.DataNotExists, "ModelId [$modelId] not exists")
        val res = this.removeById(modelId)
        if(res){
//            applicationEventPublisher.publishEvent(model)
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
//            applicationEventPublisher.publishEvent(model)
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