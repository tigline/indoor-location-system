package com.lgguan.iot.position.service

import com.lgguan.iot.position.util.file.FileUploadUtils
import com.lgguan.iot.position.util.file.MimeTypeUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

interface ISysFileService {
    @Throws(Exception::class)
    fun uploadFile(file: MultipartFile?): String?
}

@Service
class LocalSysFileServiceImpl: ISysFileService {

    @Value("\${indoor.file.prefix}")
    var localFilePrefix: String? = null

    @Value("\${indoor.file.domain}")
    var domain: String? = null

    @Value("\${indoor.file.path}")
    private val localFilePath: String? = null

    override fun uploadFile(file: MultipartFile?): String? {
        val name: String = FileUploadUtils.upload(localFilePath!!, file!!, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION)
        return domain + localFilePrefix + name
    }
}