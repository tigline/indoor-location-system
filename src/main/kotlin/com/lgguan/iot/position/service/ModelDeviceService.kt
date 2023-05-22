package com.lgguan.iot.position.service

import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.lgguan.iot.position.bean.*
import com.lgguan.iot.position.entity.ModelDevice
import com.lgguan.iot.position.mapper.IModelDeviceMapper
import org.springframework.stereotype.Service
import java.util.*


interface IModelDeviceService: IService<ModelDevice> {
    fun addModelDevice(addModelDevice: AddOrUpdateModelDevice): RestValue<Boolean>
    fun updateModelDevice(id: Int, updateModelDevice: AddOrUpdateModelDevice): RestValue<Boolean>
    fun deleteModelDevice(id: Int): RestValue<Boolean>
    fun getModelDeviceByClientId(clientId: String): ModelDevice
}

@Service
class ModelDeviceService: IModelDeviceService, ServiceImpl<IModelDeviceMapper, ModelDevice>() {

    override fun addModelDevice(addModelDevice: AddOrUpdateModelDevice): RestValue<Boolean> {
        val modelDevice = ModelDevice().apply {
            deviceId = addModelDevice.deviceId
            clientId = addModelDevice.clientId
            companyId = addModelDevice.companyId
            modelId = addModelDevice.modelId
            active = true
            createTime = Date()
        }

        return okOf(this.save(modelDevice))
    }

    override fun updateModelDevice(id: Int, updateModelDevice: AddOrUpdateModelDevice): RestValue<Boolean> {
        val modelDevice = this.getById(id)
        modelDevice ?: return failedOf(IErrorCode.DataNotExists, "updateModelDeviceId [$id] not exists")
        modelDevice.apply {
            modelDevice.deviceId = updateModelDevice.deviceId
            modelDevice.clientId = updateModelDevice.clientId
            modelDevice.companyId = updateModelDevice.companyId
            modelDevice.modelId = updateModelDevice.modelId
            modelDevice.updateTime = Date()
            modelDevice.active = updateModelDevice.active
        }

        return okOf(this.updateById(modelDevice))
    }

    override fun deleteModelDevice(id: Int): RestValue<Boolean> {
        val modelDevice = this.getById(id);
        modelDevice ?: return failedOf(IErrorCode.DataNotExists, "ModelDeviceId [$id] not exists")
        return okOf(this.removeById(modelDevice))
    }

    override fun getModelDeviceByClientId(clientId: String): ModelDevice {
        return this.getBaseMapper().getModelDeviceByClientId(clientId)
    }


}