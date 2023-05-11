package com.lgguan.iot.position.service

import cn.hutool.json.JSONUtil
import com.lgguan.iot.position.bean.*
import com.lgguan.iot.position.util.getApiKey
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ModelManageService(
    val companyService: ICompanyService,
    val modelService: IModelService,
    val modelDeviceService: IModelDeviceService,
    val gatewayInfoService: IGatewayInfoService) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun getModelInfoByClientId(authorityBody: IotAuthorityRequest): RestValue<IotAuthorityResponse> {
        log.info("authorityBody:"+ JSONUtil.toJsonStr(authorityBody))
        val apiKey: String = getApiKey()
        log.info("apiKey:$apiKey")
        try {

            val modelDevice = modelDeviceService.getModelDeviceByClientId(authorityBody.clientId)
            log.info("modelDevice:"+ JSONUtil.toJsonStr(modelDevice))
            val model = modelService.getById(modelDevice.modelId)
            log.info("model:"+ JSONUtil.toJsonStr(model))
            val company = companyService.getById(modelDevice.companyId)
            log.info("company:"+ JSONUtil.toJsonStr(company))

            val gatewayInfo = gatewayInfoService.getById(modelDevice.deviceId)
            log.info("gatewayInfo:"+ JSONUtil.toJsonStr(gatewayInfo))

            val res = IotAuthorityResponse(
                authorityBody.clientId,
                company?.companyCode?:"",
                gatewayInfo?.groupId?:"",
                model?.modelCode?:""
            )
            return okOf(res)
        }catch (e: Exception){
            log.error(e.message)
        }
        val clientId = authorityBody.clientId
        return failedOf(IErrorCode.SystemErrorFailed, "ClientId:$clientId getModelInfoByClientId exception!")
    }


}