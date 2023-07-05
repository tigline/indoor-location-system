package com.lgguan.iot.position.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.lgguan.iot.position.entity.PersonnelTypeInfo
import org.apache.ibatis.annotations.Select

/**
 *
 *
 * @author N.Liu
 **/
interface IPersonnelTypeInfoMapper: BaseMapper<PersonnelTypeInfo> {
    @Select("SELECT COUNT(*) FROM personnel_type_info WHERE type_name = #{typeName}")
    fun selectCountByTypeName(typeName: String): Int
}
