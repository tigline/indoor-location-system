package com.lgguan.iot.position.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.lgguan.iot.position.bean.IotModelResponse
import com.lgguan.iot.position.entity.Model

interface IModelMapper: BaseMapper<Model> {
    fun getAllModelInfo(): List<IotModelResponse>
}