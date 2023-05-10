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
    val beaconInfoService: IBeaconInfoService) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun getModelInfoByClientId(authorityBody: IotAuthorityRequest): RestValue<IotAuthorityResponse> {
        log.info("authorityBody:"+ JSONUtil.toJsonStr(authorityBody))
        val apiKey: String = getApiKey()
        log.info("apiKey:$apiKey")
        val modelDevice = modelDeviceService.getModelDeviceByClientId(authorityBody.clientId)
        log.info("modelDevice:"+ JSONUtil.toJsonStr(modelDevice))
        val model = modelService.getById(modelDevice.modelId)
        log.info("model:"+ JSONUtil.toJsonStr(model))
        var company = companyService.getById(modelDevice.companyId)
        log.info("company:"+ JSONUtil.toJsonStr(company))
//        val beaconInfo = beaconInfoService.getById(authorityBody.clientId)
//        beaconInfo ?: return failedOf(IErrorCode.DataNotExists, "Beacon [${authorityBody.clientId}] not exists")

        val res = company.companyCode?.let {
            model.modelCode?.let { it1 ->
                IotAuthorityResponse(
                    authorityBody.clientId,
                    it,
                    "aoa01",
                    it1,
                    200,
                    "success",
                    ""
                )
            }
        }
        return okOf(res)
    }


}