package com.lgguan.iot.position.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.baomidou.mybatisplus.core.metadata.IPage
import com.lgguan.iot.position.bean.PersonnelFillInfo
import com.lgguan.iot.position.entity.PersonnelInfo
import org.apache.ibatis.annotations.Param

/**
 *
 *
 * @author N.Liu
 **/
interface IPersonnelInfoMapper : BaseMapper<PersonnelInfo> {
    fun pagePersonnelFillInfo(page: IPage<PersonnelInfo>, @Param("searchValue") searchValue: String?):
            IPage<PersonnelFillInfo>

    fun pageDepPersonnelFillInfo(page: IPage<PersonnelInfo>, @Param("depId") depId: Long): IPage<PersonnelFillInfo>
}
