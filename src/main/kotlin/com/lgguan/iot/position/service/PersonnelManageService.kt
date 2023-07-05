package com.lgguan.iot.position.service

import cn.hutool.core.date.DateUtil
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.lgguan.iot.position.bean.*
import com.lgguan.iot.position.entity.DepartmentInfo
import com.lgguan.iot.position.entity.PersonnelInfo
import com.lgguan.iot.position.entity.PersonnelTypeInfo
import com.lgguan.iot.position.ext.convert
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(rollbackFor = [Exception::class])
class PersonnelManageService(
    val departmentService: IDepartmentInfoService,
    val personnelTypeService: IPersonnelTypeService,
    val personnelInfoService: IPersonnelInfoService,
    val beaconInfoService: IBeaconInfoService
) {
    fun toTree(departments: List<DepartmentInfo>): List<DepartmentTree> {
        val res = mutableListOf<DepartmentTree>()
        val map = mutableMapOf<Long, DepartmentTree>()
        departments.forEach { dep ->
            val element = DepartmentTree().apply {
                depId = dep.depId
                parentId = dep.parentId
                name = dep.name
            }
            val parentId = dep.parentId
            if (parentId == 0L || !map.contains(parentId)) {
                res.add(element)
            } else {
                val parent = map[parentId]
                val children = parent?.children ?: mutableListOf()
                children.add(element)
                parent?.children = children
            }
            map[dep.depId!!] = element
        }
        return res
    }

    fun treeDepartment(): List<DepartmentTree> {
        val departmentInfos: List<DepartmentInfo> = departmentService.list()
        return toTree(departmentInfos)
    }

    fun addDepartmentInfo(addDepartment: AddOrUpdateDepartment): Boolean {
        val departmentInfo = DepartmentInfo().apply {
            name = addDepartment.name
            parentId = addDepartment.parentId
        }
        return departmentService.save(departmentInfo)
    }

    fun updateDepartmentInfo(depId: Long, updateDepartment: AddOrUpdateDepartment): RestValue<Boolean> {
        val departmentInfo = departmentService.getById(depId)
            ?: return failedOf(IErrorCode.DataNotExists, "DepId [$depId] not exists")
        departmentInfo.name = updateDepartment.name
        val update = departmentService.updateById(departmentInfo)
        return okOf(update)
    }

    fun deleteDepartmentInfo(depId: Long): RestValue<Boolean> {
        departmentService.getById(depId)
            ?: return failedOf(IErrorCode.DataNotExists, "DepId [$depId] not exists")
        val subDepCount =
            departmentService.count(KtQueryWrapper(DepartmentInfo::class.java).eq(DepartmentInfo::parentId, depId))
        if (subDepCount > 0) {
            return failedOf(IErrorCode.DataExists, "Sub-departments exist")
        }
        val personnelCount =
            personnelInfoService.count(KtQueryWrapper(PersonnelInfo::class.java).eq(PersonnelInfo::depId, depId))
        if (personnelCount > 0) {
            return failedOf(IErrorCode.DataExists, "Related personnel of the department")
        }
        val remove = departmentService.removeById(depId)
        return okOf(remove)
    }

    fun pagePersonnelTypeInfos(hidePicture: Boolean = false, pageLimit: PageLimit): PageResult<PersonnelTypeInfo> {
        return personnelTypeService.page(pageLimit.convert(), KtQueryWrapper(PersonnelTypeInfo::class.java)
            .select(PersonnelTypeInfo::class.java) { if (hidePicture) it.property != "picture" else true })
            .convert()
    }

    fun addPersonnelTypeInfo(addPersonnelType: AddOrUpdatePersonnelType): Boolean {
        val personnelTypeInfo = PersonnelTypeInfo().apply {
            typeName = addPersonnelType.typeName
            picture = addPersonnelType.picture
            createTime = DateUtil.currentSeconds()
        }
        return personnelTypeService.save(personnelTypeInfo)
    }

    fun deletePersonnelTypeInfo(typeId: Long): RestValue<Boolean> {
        personnelTypeService.getById(typeId) ?: return failedOf(IErrorCode.DataNotExists, "TypeId [$typeId] not exists")
        val res = personnelTypeService.removeById(typeId)
        return okOf(res)
    }

    fun updatePersonnelTypeInfo(typeId: Long, updatePersonnelType: AddOrUpdatePersonnelType): RestValue<Boolean> {
        val personnelTypeInfo = personnelTypeService.getById(typeId)
        personnelTypeInfo ?: return failedOf(IErrorCode.DataNotExists, "TypeId [$typeId] not exists")
        personnelTypeInfo.apply {
            typeName = updatePersonnelType.typeName
            picture = updatePersonnelType.picture
        }
        val update = personnelTypeService.updateById(personnelTypeInfo)
        return okOf(update)
    }

    fun pageDepPersonnelInfos(depId: Long, pageLimit: PageLimit): PageResult<PersonnelFillInfo> {
        return personnelInfoService.pageDepPersonnelFillInfo(depId, pageLimit.convert()).convert()
    }

    fun pagePersonnelInfos(searchValue: String?, pageLimit: PageLimit): PageResult<PersonnelFillInfo> {
        return personnelInfoService.pagePersonnelFillInfo(searchValue, pageLimit.convert()).convert()
    }

    fun addPersonnelInfo(addPersonnel: AddOrUpdatePersonnel): Boolean {
        val personnelInfo = PersonnelInfo().apply {
            name = addPersonnel.name
            sex = addPersonnel.sex
            tag = addPersonnel.tag
            typeId = addPersonnel.typeId
            depId = addPersonnel.depId
            avatar = addPersonnel.avatar
            createTime = DateUtil.currentSeconds()
        }
        if (!personnelInfo.tag.isNullOrEmpty()) {
            beaconInfoService.changeBindStatus(personnelInfo.tag!!, BindStatus.Bound)
        }
        return personnelInfoService.save(personnelInfo)
    }

    fun updatePersonnelInfo(personnelId: Long, updatePersonnel: AddOrUpdatePersonnel): RestValue<Boolean> {
        val personnelInfo = personnelInfoService.getById(personnelId)
        personnelInfo ?: return failedOf(IErrorCode.DataNotExists, "PersonnelId [$personnelId] not exists")
        if (personnelInfo.tag != updatePersonnel.tag) {
            if (!personnelInfo.tag.isNullOrEmpty()) {
                beaconInfoService.changeBindStatus(personnelInfo.tag!!, BindStatus.Unbound)
            }
            if (!updatePersonnel.tag.isNullOrEmpty()) {
                beaconInfoService.changeBindStatus(updatePersonnel.tag, BindStatus.Bound)
            }
        }
        personnelInfo.apply {
            name = updatePersonnel.name
            sex = updatePersonnel.sex
            tag = updatePersonnel.tag
            typeId = updatePersonnel.typeId
            depId = updatePersonnel.depId
            avatar = updatePersonnel.avatar
        }
        val update = personnelInfoService.updateById(personnelInfo)
        return okOf(update)
    }

    fun deletePersonnelInfo(personnelId: Long): RestValue<Boolean> {
        val personnelInfo = personnelInfoService.getById(personnelId)
        personnelInfo ?: return failedOf(IErrorCode.DataNotExists, "PersonnelId [$personnelId] not exists")
        if (!personnelInfo.tag.isNullOrEmpty()) {
            beaconInfoService.changeBindStatus(personnelInfo.tag!!, BindStatus.Unbound)
        }
        val remove = personnelInfoService.removeById(personnelId)
        return okOf(remove)
    }

    fun unbindTag(personnelId: Long, tag: String): RestValue<Boolean> {
        val personnelInfo = personnelInfoService.getById(personnelId)
        personnelInfo ?: return failedOf(IErrorCode.DataNotExists, "PersonnelId [$personnelId] not exists")
        beaconInfoService.changeBindStatus(tag, BindStatus.Unbound)
        personnelInfo.tag = null
        val update = personnelInfoService.updateById(personnelInfo)
        return okOf(update)
    }


}
