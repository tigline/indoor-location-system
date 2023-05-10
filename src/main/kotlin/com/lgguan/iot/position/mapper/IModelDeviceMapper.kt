package com.lgguan.iot.position.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.lgguan.iot.position.entity.ModelDevice

interface IModelDeviceMapper: BaseMapper<ModelDevice> {
    fun getModelDeviceByClientId(clientId: String): ModelDevice
}