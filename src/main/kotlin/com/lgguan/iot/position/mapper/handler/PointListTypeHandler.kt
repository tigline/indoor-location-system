package com.lgguan.iot.position.mapper.handler

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler.getObjectMapper
import com.fasterxml.jackson.core.type.TypeReference
import com.lgguan.iot.position.util.Point
import java.io.IOException


/**
 *
 *
 * @author N.Liu
 **/
class PointListTypeHandler: AbstractJsonTypeHandler<List<Point>>() {
    override fun parse(json: String?): List<Point> {
        return try {
            getObjectMapper().readValue(json, object: TypeReference<List<Point>>(){})
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    override fun toJson(obj: List<Point>): String {
        return getObjectMapper().writeValueAsString(obj)
    }
}
