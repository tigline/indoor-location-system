package com.lgguan.iot.position.service

import cn.hutool.json.JSONUtil
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.lgguan.iot.position.bean.*
import com.lgguan.iot.position.entity.Company
import com.lgguan.iot.position.ext.convert
import com.lgguan.iot.position.mapper.ICompanyMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

interface ICompanyService: IService<Company> {
    fun pageCompany(companyName: String?, companyCode: String?, pageLimit: PageLimit): PageResult<Company>
//    fun addCompany(addCompany: AddOrUpdateCompany): RestValue<Boolean>
//    fun updateCompany(companyId: Int, updateCompany: AddOrUpdateCompany): RestValue<Boolean>
    fun deleteCompany(companyId: Int): RestValue<Boolean>
    fun updateCompanyActive(companyId: Int, active: Boolean): RestValue<Boolean>
    fun addCompanySync(companyList: List<Map<String, Any>>): RestValue<Boolean>
    fun listCompany(lang: String): List<Map<String, String>>
}

@Service
class CompanyService : ICompanyService, ServiceImpl<ICompanyMapper, Company>() {
    private val log = LoggerFactory.getLogger(javaClass)
    override fun pageCompany(companyName: String?, companyCode: String?, pageLimit: PageLimit): PageResult<Company> {
        return page(
            pageLimit.convert(), KtQueryWrapper(Company::class.java)
                .eq(!companyCode.isNullOrEmpty(), Company::companyCode, companyCode)
                .like(!companyName.isNullOrEmpty(), Company::companyName, companyName)
                .eq(Company::active, true)
        ).convert()
    }

//    override fun addCompany(addCompany: AddOrUpdateCompany): RestValue<Boolean> {
//        val companyCodeCount = this.count(KtQueryWrapper(Company::class.java).eq(Company::companyCode, addCompany.companyCode))
//        if (companyCodeCount > 0) {
//            return failedOf(IErrorCode.DataExists, "companyCode is exist data")
//        }
//
//        val company = Company().apply {
//            companyCode = addCompany.companyCode
//            companyName = addCompany.companyName
//            simpleName = addCompany.simpleName
//            contactName = addCompany.contactName
//            contactPhone = addCompany.contactPhone
//            beginCreateTime = Date()
//            active = true
//            createTime = Date()
//        }
//        return okOf(this.save(company))
//    }
//
//    override fun updateCompany(companyId: Int, updateCompany: AddOrUpdateCompany): RestValue<Boolean> {
//        val company = this.getById(companyId)
//        company ?: return failedOf(IErrorCode.DataNotExists, "CompanyId [$companyId] not exists")
//
//        val companyCodeCount = this.count(KtQueryWrapper(Company::class.java).eq(Company::companyCode, updateCompany.companyCode))
//        if (companyCodeCount > 0) {
//            return failedOf(IErrorCode.DataExists, "companyCode is exist data")
//        }
//
//        company.apply {
//            companyCode = updateCompany.companyCode
//            companyName = updateCompany.companyName
//            simpleName = updateCompany.simpleName
//            contactName = updateCompany.contactName
//            contactPhone = updateCompany.contactPhone
//            updateTime = Date()
//        }
//        return okOf(this.updateById(company))
//    }

    override fun deleteCompany(companyId: Int): RestValue<Boolean> {
        val company = this.getById(companyId)
        company ?: return failedOf(IErrorCode.DataNotExists, "CompanyId [$companyId] not exists")
        val res = this.removeById(companyId)
        return okOf(res)
    }

    override fun updateCompanyActive(companyId: Int, active: Boolean): RestValue<Boolean> {
        val company = this.getById(companyId)
        company ?: return failedOf(IErrorCode.DataNotExists, "CompanyId [$companyId] not exists")
        company.apply {
            company.active = active
            updateTime = Date()
        }
        return okOf(this.updateById(company))
    }

    override fun addCompanySync(companyList: List<Map<String, Any>>): RestValue<Boolean> {
        try {
            companyList.forEach { map -> saveCompany(map) }
            return okOf(true)
        } catch (e: Exception) {
            log.error(e.message)
        }
        return failedOf(IErrorCode.SystemErrorFailed, IErrorCode.SystemErrorFailed.message)
    }

    override fun listCompany(lang: String): List<Map<String, String>> {
        var companyList = this.list(KtQueryWrapper(Company::class.java).eq(Company::active, true))
        var companys = ArrayList<Map<String, String>>()
        if(companyList != null && companyList.size > 0){
            companyList.stream().forEach{it ->
                var item = hashMapOf<String, String>()
                it.companyCode?.let { it1 -> item.put("value", it1) }
                item.put("label", it.companyName?.get(lang).toString())
                companys.add(item)
            }
        }
        return companys
    }

    fun saveCompany(data: Map<String, Any>) {
        var key: String = data.keys.stream().findFirst().get()
        var value: Map<String, Any> = data.get(key) as Map<String, Any>

        val companyEntity = this.getOne(KtQueryWrapper(Company::class.java).eq(Company::companyCode, key))
        if (Objects.nonNull(companyEntity)){
            var nameEN: String = value.get("en").toString()
            var nameCN: String = value.get("cn").toString()
            var nameJP: String = value.get("jp").toString()

            var companyJSONName = companyEntity.companyName
            if (companyJSONName != null
                && (!companyJSONName.get("cn").toString().equals(nameCN)
                        || !companyJSONName.get("en").toString().equals(nameEN)
                        || !companyJSONName.get("jp").toString().equals(nameJP))) {
                companyEntity.apply {
                    companyCode = key
                    companyName = JSONUtil.parseObj(value)
                    simpleName = value.get("en").toString()
                    updateTime = Date()
                }
                this.updateById(companyEntity)
                System.out.print("111111111111111")
            }
        } else {
            val entity = Company().apply {
                companyCode = key
                companyName = JSONUtil.parseObj(value)
                simpleName = value.get("en").toString()
                beginCreateTime = Date()
                active = true
                createTime = Date()
            }
            this.save(entity)
        }
    }
}