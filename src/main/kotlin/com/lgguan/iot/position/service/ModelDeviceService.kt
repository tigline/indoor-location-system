package com.lgguan.iot.position.service

import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.lgguan.iot.position.bean.AddOrUpdateModelDevice
import com.lgguan.iot.position.bean.RestValue
import com.lgguan.iot.position.entity.ModelDevice
import com.lgguan.iot.position.mapper.IModelDeviceMapper
import org.springframework.stereotype.Service


interface IModelDeviceService: IService<ModelDevice> {
    fun addModelDevice(addModelDevice: AddOrUpdateModelDevice): RestValue<Boolean>
    fun updateModelDevice(id: Int, updateModelDevice: AddOrUpdateModelDevice): RestValue<Boolean>
    fun deleteModelDevice(id: Int): RestValue<Boolean>
    fun updateModelDeviceActive(id: Int, active: Boolean): RestValue<Boolean>
    fun getModelDeviceByClientId(clientId: String): ModelDevice
}

@Service
class ModelDeviceService: IModelDeviceService, ServiceImpl<IModelDeviceMapper, ModelDevice>() {

    override fun addModelDevice(addModelDevice: AddOrUpdateModelDevice): RestValue<Boolean> {
        TODO("Not yet implemented")
    }

    override fun updateModelDevice(id: Int, updateModelDevice: AddOrUpdateModelDevice): RestValue<Boolean> {
        TODO("Not yet implemented")
    }

    override fun deleteModelDevice(id: Int): RestValue<Boolean> {
        TODO("Not yet implemented")
    }

    override fun updateModelDeviceActive(id: Int, active: Boolean): RestValue<Boolean> {
        TODO("Not yet implemented")
    }

    override fun getModelDeviceByClientId(clientId: String): ModelDevice {
        return this.getBaseMapper().getModelDeviceByClientId(clientId)
    }


}