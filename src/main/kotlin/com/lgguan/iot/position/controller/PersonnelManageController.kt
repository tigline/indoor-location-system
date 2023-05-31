package com.lgguan.iot.position.controller

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.lgguan.iot.position.bean.*
import com.lgguan.iot.position.entity.PersonnelInfo
import com.lgguan.iot.position.entity.PersonnelTypeInfo
import com.lgguan.iot.position.service.PersonnelManageService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springdoc.core.annotations.ParameterObject
import org.springframework.web.bind.annotation.*

@Tag(name = "人员管理")
@RestController
@RequestMapping("/api/v1")
class PersonnelManageController(val personnelManageService: PersonnelManageService) {
    @Operation(summary = "获取所有部门")
    @GetMapping("/treeDepartment")
    fun treeDepartment(): RestValue<List<DepartmentTree>> {
        val tree = personnelManageService.treeDepartment()
        return okOf(tree)
    }

    @Operation(summary = "添加部门")
    @PostMapping("/departments")
    fun addDepartment(@Valid @RequestBody addDepartment: AddOrUpdateDepartment): RestValue<Boolean> {
        val res = personnelManageService.addDepartmentInfo(addDepartment)
        return okOf(res)
    }

    @Operation(summary = "编辑部门信息")
    @PutMapping("/departments/{depId}")
    fun updateDepartment(
        @PathVariable depId: Long,
        @Valid @RequestBody updateDepartment: AddOrUpdateDepartment
    ): RestValue<Boolean> {
        return personnelManageService.updateDepartmentInfo(depId, updateDepartment)
    }

    @Operation(summary = "删除部门")
    @DeleteMapping("/departments/{depId}")
    fun deleteDepartment(@PathVariable depId: Long): RestValue<Boolean> {
        return personnelManageService.deleteDepartmentInfo(depId)
    }

    @Operation(summary = "分页获取人员类型")
    @GetMapping("/personnelTypes")
    fun pagePersonnelType(hidePicture: Boolean? = false, @ParameterObject pageLimit: PageLimit):
            RestValue<PageResult<PersonnelTypeInfo>> {
        val res = personnelManageService.pagePersonnelTypeInfos(hidePicture ?: false, pageLimit)
        return okOf(res)
    }

    @Operation(summary = "添加人员分类")
    @PostMapping("/personnelTypes")
    fun addPersonnelType(@Valid @RequestBody addPersonnelType: AddOrUpdatePersonnelType): RestValue<Boolean> {
        val res = personnelManageService.addPersonnelTypeInfo(addPersonnelType)
        return okOf(res)
    }

    @Operation(summary = "删除人员分类")
    @DeleteMapping("/personnelTypes/{typeId}")
    fun deletePersonnelType(@PathVariable typeId: Long): RestValue<Boolean> {
        var count = personnelManageService.personnelInfoService.count(
            KtQueryWrapper(PersonnelInfo::class.java)
                .eq(PersonnelInfo::typeId, typeId)
        )
        if (count > 0) {
            return failedOf(IErrorCode.TypeIdIsReferenced)
        }
        return personnelManageService.deletePersonnelTypeInfo(typeId)
    }

    @Operation(summary = "编辑人员分类")
    @PutMapping("/personnelTypes/{typeId}")
    fun updatePersonnelType(
        @PathVariable typeId: Long,
        @Valid @RequestBody updatePersonnelType: AddOrUpdatePersonnelType
    ): RestValue<Boolean> {
        return personnelManageService.updatePersonnelTypeInfo(typeId, updatePersonnelType)
    }

    @Operation(summary = "分页获取部门人员信息")
    @GetMapping("/dep/personnel")
    fun pageDepPersonnel(
        @Parameter(required = true) depId: Long = 0,
        @ParameterObject pageLimit: PageLimit
    ): RestValue<PageResult<PersonnelFillInfo>> {
        val res = personnelManageService.pageDepPersonnelInfos(depId, pageLimit)
        return okOf(res)
    }

    @Operation(summary = "分页获取人员信息")
    @GetMapping("/personnel")
    fun pagePersonnel(
        searchValue: String? = null,
        @ParameterObject pageLimit: PageLimit
    ): RestValue<PageResult<PersonnelFillInfo>> {
        val res = personnelManageService.pagePersonnelInfos(searchValue, pageLimit)
        return okOf(res)
    }

    @Operation(summary = "添加人员信息")
    @PostMapping("/personnel")
    fun addPersonnel(@Valid @RequestBody addPersonnel: AddOrUpdatePersonnel): RestValue<Boolean> {
        val res = personnelManageService.addPersonnelInfo(addPersonnel)
        return okOf(res)
    }

    @Operation(summary = "编辑人员信息")
    @PutMapping("/personnel/{personnelId}")
    fun updatePersonnel(
        @PathVariable personnelId: Long,
        @Valid @RequestBody updatePersonnel: AddOrUpdatePersonnel
    ): RestValue<Boolean> {
        return personnelManageService.updatePersonnelInfo(personnelId, updatePersonnel)
    }

    @Operation(summary = "删除人员信息")
    @DeleteMapping("/personnel/{personnelId}")
    fun deletePersonnel(@PathVariable personnelId: Long): RestValue<Boolean> {
        return personnelManageService.deletePersonnelInfo(personnelId)
    }

    @Operation(summary = "解绑人员标签")
    @DeleteMapping("/personnel/{personnelId}/{tag}")
    fun unbindTag(@PathVariable personnelId: Long, @PathVariable tag: String): RestValue<Boolean> {
        return personnelManageService.unbindTag(personnelId, tag)
    }

}
