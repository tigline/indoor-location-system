package com.lgguan.iot.position.util.file

import com.lgguan.iot.position.exception.file.FileNameLengthLimitExceededException
import com.lgguan.iot.position.exception.file.FileSizeLimitExceededException
import com.lgguan.iot.position.exception.file.InvalidExtensionException
import org.apache.commons.io.FilenameUtils
import org.slf4j.LoggerFactory
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.nio.file.Paths
import java.util.*

/**
 * 文件上传工具类
 *
 */
object FileUploadUtils {
    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * 默认大小 200M
     */
    const val DEFAULT_MAX_SIZE = (200 * 1024 * 1024).toLong()

    /**
     * 默认的文件名最大长度 100
     */
    const val DEFAULT_FILE_NAME_LENGTH = 100

    /**
     * 文件上传
     *
     * @param baseDir 相对应用的基目录
     * @param file 上传的文件
     * @param allowedExtension 上传文件类型
     * @return 返回上传成功的文件名
     * @throws FileSizeLimitExceededException 如果超出最大大小
     * @throws FileNameLengthLimitExceededException 文件名太长
     * @throws IOException 比如读写文件出错时
     * @throws InvalidExtensionException 文件校验异常
     */
    @Throws(
        FileSizeLimitExceededException::class,
        IOException::class,
        FileNameLengthLimitExceededException::class,
        InvalidExtensionException::class
    )
    fun upload(baseDir: String, file: MultipartFile, allowedExtension: Array<String>?): String {
        val fileNameLength = Objects.requireNonNull<String>(file.getOriginalFilename()).length
        if (fileNameLength > DEFAULT_FILE_NAME_LENGTH) {
            throw FileNameLengthLimitExceededException(DEFAULT_FILE_NAME_LENGTH)
        }
        assertAllowed(file, allowedExtension)
        val fileName = extractFilename(file)
        val absPath = getAbsoluteFile(baseDir, fileName).absolutePath
        log.info("absPath:$absPath")
        file.transferTo(Paths.get(absPath))
        return getPathFileName(fileName)
    }

    /**
     * 编码文件名 2023/05/26/
     */
    fun extractFilename(file: MultipartFile): String? {
        log.info(DateUtils.datePath())
        log.info(FilenameUtils.getBaseName(file.originalFilename))
        log.info(Seq.uploadSeqType)
        log.info(FileTypeUtils.getExtension(file))

        val sBuffer = StringBuffer()
        sBuffer.append(DateUtils.datePath()).append("/")
            .append(FilenameUtils.getBaseName(file.originalFilename)).append("_")
            .append(Seq.getId(Seq.uploadSeqType)).append(".").append(FileTypeUtils.getExtension(file))
        return sBuffer.toString()
    }

    @Throws(IOException::class)
    private fun getAbsoluteFile(uploadDir: String, fileName: String?): File {
        val desc = File(uploadDir + File.separator + fileName)
        if (!desc.exists()) {
            if (!desc.parentFile.exists()) {
                desc.parentFile.mkdirs()
            }
        }
        return if (desc.isAbsolute) desc else desc.absoluteFile
    }

    @Throws(IOException::class)
    private fun getPathFileName(fileName: String?): String {
        return "/$fileName"
    }

    /**
     * 文件大小校验
     *
     * @param file 上传的文件
     * @throws FileSizeLimitExceededException 如果超出最大大小
     * @throws InvalidExtensionException 文件校验异常
     */
    @Throws(FileSizeLimitExceededException::class, InvalidExtensionException::class)
    fun assertAllowed(file: MultipartFile, allowedExtension: Array<String>?) {
        val size: Long = file.getSize()
        if (size > DEFAULT_MAX_SIZE) {
            throw FileSizeLimitExceededException(DEFAULT_MAX_SIZE / 1024 / 1024)
        }
        val fileName: String = file.getOriginalFilename()!!
        val extension = FileTypeUtils.getExtension(file)
        if (allowedExtension != null && !isAllowedExtension(extension, allowedExtension)) {
            if (allowedExtension == MimeTypeUtils.IMAGE_EXTENSION) {
                throw InvalidExtensionException.InvalidImageExtensionException(
                    allowedExtension, extension,
                    fileName
                )
            } else if (allowedExtension == MimeTypeUtils.FLASH_EXTENSION) {
                throw InvalidExtensionException.InvalidFlashExtensionException(
                    allowedExtension, extension,
                    fileName
                )
            } else if (allowedExtension == MimeTypeUtils.MEDIA_EXTENSION) {
                throw InvalidExtensionException.InvalidMediaExtensionException(
                    allowedExtension, extension,
                    fileName
                )
            } else if (allowedExtension == MimeTypeUtils.VIDEO_EXTENSION) {
                throw InvalidExtensionException.InvalidVideoExtensionException(
                    allowedExtension, extension,
                    fileName
                )
            } else {
                throw InvalidExtensionException(allowedExtension, extension, fileName)
            }
        }
    }

    /**
     * 判断MIME类型是否是允许的MIME类型
     *
     * @param extension 上传文件类型
     * @param allowedExtension 允许上传文件类型
     * @return true/false
     */
    fun isAllowedExtension(extension: String?, allowedExtension: Array<String>): Boolean {
        for (str in allowedExtension) {
            if (str.equals(extension, ignoreCase = true)) {
                return true
            }
        }
        return false
    }
}