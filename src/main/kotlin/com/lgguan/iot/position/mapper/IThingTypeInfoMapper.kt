package com.lgguan.iot.position.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.lgguan.iot.position.entity.ThingTypeInfo
import org.apache.ibatis.annotations.Select

/**
 *
 *
 * @author N.Liu
 **/
interface IThingTypeInfoMapper: BaseMapper<ThingTypeInfo> {
    @Select("SELECT COUNT(*) FROM thing_type_info WHERE type_name = #{typeName}")
    fun selectCountByTypeName(typeName: String): Int
}
