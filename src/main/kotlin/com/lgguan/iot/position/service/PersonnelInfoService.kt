package com.lgguan.iot.position.service

import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.lgguan.iot.position.bean.PersonnelFillInfo
import com.lgguan.iot.position.entity.PersonnelInfo
import com.lgguan.iot.position.mapper.IPersonnelInfoMapper
import org.springframework.stereotype.Service

/**
 *
 *
 * @author N.Liu
 **/
interface IPersonnelInfoService: IService<PersonnelInfo> {
    fun pagePersonnelFillInfo(searchValue: String?, page: IPage<PersonnelInfo>): IPage<PersonnelFillInfo>
    fun pageDepPersonnelFillInfo(depId: Long, page: IPage<PersonnelInfo>): IPage<PersonnelFillInfo>
    fun removeTag(tag: String)
}

@Service
class PersonnelInfoServiceImpl: IPersonnelInfoService, ServiceImpl<IPersonnelInfoMapper, PersonnelInfo>() {
    override fun pageDepPersonnelFillInfo(depId: Long, page: IPage<PersonnelInfo>): IPage<PersonnelFillInfo> {
        return this.getBaseMapper().pageDepPersonnelFillInfo(page, depId)
    }

    override fun pagePersonnelFillInfo(searchValue: String?, page: IPage<PersonnelInfo>): IPage<PersonnelFillInfo> {
        return this.getBaseMapper().pagePersonnelFillInfo(page, searchValue)
    }

    override fun removeTag(tag: String) {
        this.remove(KtQueryWrapper(PersonnelInfo::class.java).eq(PersonnelInfo::tag, tag))
    }

}
