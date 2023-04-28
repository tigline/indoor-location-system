package com.lgguan.iot.position.controller

import com.lgguan.iot.position.bean.AddOrUpdateCompany
import com.lgguan.iot.position.bean.RestValue
import com.lgguan.iot.position.bean.okOf
import com.lgguan.iot.position.entity.Company
import com.lgguan.iot.position.service.CompanyService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*


@Tag(name = "公司管理")
@RestController
@RequestMapping("/api/v1")
class CompanyController(val companyService: CompanyService){

    @Operation(summary = "获取公司列表")
    @GetMapping("/companys")
    fun listCompany(companyName: String? = null, companyCode: String? = null): RestValue<List<Company>> {
        val res = companyService.listCompany(companyName, companyCode)
        return okOf(res)
    }

    @Operation(summary = "添加公司")
    @PostMapping("/company")
    fun addBuilding(@Valid @RequestBody addCompany: AddOrUpdateCompany): RestValue<Boolean> {
        return companyService.addCompany(addCompany)
    }

    @Operation(summary = "编辑公司")
    @PutMapping("/company/{companyId}")
    fun updateCompany(
        @PathVariable companyId: Int,
        @Valid @RequestBody updateCompany: AddOrUpdateCompany
    ): RestValue<Boolean> {
        return companyService.updateCompany(companyId, updateCompany)
    }

    @Operation(summary = "删除公司")
    @DeleteMapping("/company/{companyId}")
    fun deleteCompany(@PathVariable companyId: Int): RestValue<Boolean> {
        return companyService.deleteCompany(companyId)
    }

    @Operation(summary = "操作公司有效/无效")
    @PutMapping("/company/{companyId}/{active}")
    fun updateCompanyActive(
        @PathVariable companyId: Int,
        @PathVariable active: Boolean
    ): RestValue<Boolean> {
        return companyService.updateCompanyActive(companyId, active)
    }
}