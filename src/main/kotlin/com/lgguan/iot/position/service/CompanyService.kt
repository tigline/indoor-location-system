package com.lgguan.iot.position.service

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.lgguan.iot.position.bean.*
import com.lgguan.iot.position.entity.Company
import com.lgguan.iot.position.mapper.ICompanyMapper
import org.springframework.stereotype.Service
import java.util.*

interface ICompanyService: IService<Company> {
    fun listCompany(companyName: String?, companyCode: String?): List<Company>
    fun addCompany(addCompany: AddOrUpdateCompany): RestValue<Boolean>
    fun updateCompany(companyId: Int, updateCompany: AddOrUpdateCompany): RestValue<Boolean>
    fun deleteCompany(companyId: Int): RestValue<Boolean>
    fun updateCompanyActive(companyId: Int, active: Boolean): RestValue<Boolean>
}

@Service
class CompanyService : ICompanyService, ServiceImpl<ICompanyMapper, Company>() {

    override fun listCompany(companyName: String?, companyCode: String?): List<Company> {
        return this.list(
            KtQueryWrapper(Company::class.java)
                .like(!companyName.isNullOrEmpty(), Company::companyName, companyName)
                .eq(!companyCode.isNullOrEmpty(), Company::companyCode, companyCode)
        )
    }

    override fun addCompany(addCompany: AddOrUpdateCompany): RestValue<Boolean> {
        val companyCodeCount = this.count(KtQueryWrapper(Company::class.java).eq(Company::companyCode, addCompany.companyCode))
        if (companyCodeCount > 0) {
            return failedOf(IErrorCode.DataExists, "companyCode is exist data")
        }

        val company = Company().apply {
            companyCode = addCompany.companyCode
            companyName = addCompany.companyName
            simpleName = addCompany.simpleName
            contactName = addCompany.contactName
            contactPhone = addCompany.contactPhone
            beginCreateTime = Date()
            active = true
            createTime = Date()
        }
        return okOf(this.save(company))
    }

    override fun updateCompany(companyId: Int, updateCompany: AddOrUpdateCompany): RestValue<Boolean> {
        val company = this.getById(companyId);
        company ?: return failedOf(IErrorCode.DataNotExists, "CompanyId [$companyId] not exists")
        company.apply {
            companyCode = updateCompany.companyCode
            companyName = updateCompany.companyName
            simpleName = updateCompany.simpleName
            contactName = updateCompany.contactName
            contactPhone = updateCompany.contactPhone
            updateTime = Date()
        }
        return okOf(this.updateById(company))
    }

    override fun deleteCompany(companyId: Int): RestValue<Boolean> {
        val company = this.getById(companyId);
        company ?: return failedOf(IErrorCode.DataNotExists, "CompanyId [$companyId] not exists")
        val res = this.removeById(companyId)
        return okOf(res)
    }

    override fun updateCompanyActive(companyId: Int, active: Boolean): RestValue<Boolean> {
        val company = this.getById(companyId);
        company ?: return failedOf(IErrorCode.DataNotExists, "CompanyId [$companyId] not exists")
        company.apply {
            company.active = active
            updateTime = Date()
        }
        return okOf(this.updateById(company))
    }
}