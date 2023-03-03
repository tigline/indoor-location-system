package com.lgguan.iot.position.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.baomidou.mybatisplus.core.metadata.IPage
import com.lgguan.iot.position.bean.FenceAndMapInfo
import com.lgguan.iot.position.bean.QueryFenceAndMapParam
import com.lgguan.iot.position.entity.FenceInfo
import org.apache.ibatis.annotations.Param

/**
 *
 *
 * @author N.Liu
 **/
interface IFenceInfoMapper: BaseMapper<FenceInfo> {
    fun pageFenceAndMap(page: IPage<FenceInfo>, @Param("param") param: QueryFenceAndMapParam): IPage<FenceAndMapInfo>
}
