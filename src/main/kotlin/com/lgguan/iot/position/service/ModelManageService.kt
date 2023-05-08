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

    fun getModelInfoByDeviceId(authorityBody: IotAuthorityRequest): RestValue<IotAuthorityResponse> {
        log.info("authorityBody:"+ JSONUtil.toJsonStr(authorityBody))
        val apiKey: String = getApiKey()
        log.info("apiKey:$apiKey")
        val beaconInfo = beaconInfoService.getById(authorityBody.deviceId)
        beaconInfo ?: return failedOf(IErrorCode.DataNotExists, "Beacon [$authorityBody.deviceId] not exists")
        val modelDevice = modelDeviceService.getModelDeviceById(beaconInfo.deviceId!!)
        val model = modelService.getById(modelDevice.modelId)
        var company = companyService.getById(modelDevice.companyId)

        val res = company.companyCode?.let {
            model.modelId?.let { it1 ->
                IotAuthorityResponse(
                    authorityBody.deviceId,
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