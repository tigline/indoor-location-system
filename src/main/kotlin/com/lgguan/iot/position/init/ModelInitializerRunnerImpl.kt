package com.lgguan.iot.position.init

import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.stream.Collectors

@Component
class ModelInitializerRunnerImpl : CommandLineRunner {

    private val log = LoggerFactory.getLogger(javaClass)

    private val MODELPATHS = arrayOf("AOA01.json")
    private val PATH = "model"

    override fun run(vararg args: String?) {
        initModelParse()
    }

    private fun initModelParse() {
        try {
            for (modelPath in MODELPATHS) {
                val protocolResource: Resource = ClassPathResource(StringUtils.joinWith("/", PATH, modelPath))
                val fileName = modelPath.substring(0, modelPath.lastIndexOf("."))
                val arr = fileName.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val modelCode = arr[0]
                val ver = if (arr.size > 1) arr[1] else null
                val protocolReader = BufferedReader(InputStreamReader(protocolResource.inputStream))
                val protocolContent = protocolReader.lines().collect(Collectors.joining())
            }
        } catch (e: Exception) {
            log.error(
                "ModelInitializerRunnerImpl.init, Reading protocol file parse exception, params={{}}, json={}, message={}",
                StringUtils.EMPTY,
                StringUtils.EMPTY,
                e
            )
        }
    }
}