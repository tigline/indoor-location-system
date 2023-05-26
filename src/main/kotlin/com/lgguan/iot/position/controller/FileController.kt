package com.lgguan.iot.position.controller

import com.lgguan.iot.position.bean.FileUpload
import com.lgguan.iot.position.bean.RestValue
import com.lgguan.iot.position.bean.okOf
import com.lgguan.iot.position.service.ISysFileService
import com.lgguan.iot.position.util.file.FileUtils
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@Tag(name = "文件上传管理")
@RestController
@RequestMapping("/api/v1")
class FileController(val sysFileService: ISysFileService) {

    private val log = LoggerFactory.getLogger(javaClass)

    @PostMapping("upload")
    fun upload(file: MultipartFile?): RestValue<FileUpload?>? {
        val url: String = sysFileService.uploadFile(file)!!
        val sysFile = FileUpload(
            name = FileUtils.getName(url),
            url = url
        )
        return okOf(sysFile)
    }

}