package com.lvonce.lightfileserver.core

import io.minio.MinioClient
import io.minio.ObjectStat
import io.minio.errors.ErrorResponseException
import com.lvonce.lightfileserver.general.IdGenerator
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.core.io.InputStreamResource


@Service
class FileStorageServiceImpl: FileStorageService {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(FileStorageServiceImpl::class.java)
        val nameGenerator = IdGenerator

//        val minioClient = MinioClient(
//                "http://47.107.45.198:9000",
//                "AKTAIOSFODNN7EXAMPLE",
//                "wJalrXUtnFEMI/K7MDENG/bPxEfiCYEXAMPLEKEY")

        val properties: FileServiceProperties? by lazy {
            FileServiceProperties.instance
        }

        val minioClient by lazy {
            MinioClient(properties!!.endpoint, properties!!.accessKey, properties!!.secretKey)
        }

    }

    override fun fetch(bucketName: String, fileName: String): Resource? {
        try {
            val stream = minioClient.getObject(bucketName, fileName)
            return InputStreamResource(stream)
        } catch (ex:Exception) {
            logger.warn("fetch($bucketName, $fileName) - exception")
        }
        return null
    }

    fun generateNewName(fileName:String):String {
        val fileNameSplit:List<String> = fileName.split('.')
        val firstNamePart = "${fileNameSplit[0]}(${nameGenerator.nextId()})"
        val builder = StringBuffer(firstNamePart)
        for (i in 1 until fileNameSplit.size) {
            builder.append(".")
            builder.append(fileNameSplit[i])
        }
        return builder.toString()
    }

    data class FileStat(val state:ObjectStat?, val fileExist:Boolean)
    fun getFileStat(bucketName: String, fileName: String): FileStat {
        try {
            return FileStat(minioClient.statObject(bucketName, fileName), true)
        } catch (ex: ErrorResponseException) {
            logger.info("getFileStat($bucketName, $fileName) - exception")
        }
        return FileStat(null, false)
    }

    override fun save(bucketName: String, file: MultipartFile): String? {
        val fileName:String = file.originalFilename?:""
        if (file.isEmpty || file.originalFilename == "") {
            return null
        }

        val isBucketExist = minioClient.bucketExists(bucketName)
        if (!isBucketExist) {
            minioClient.makeBucket(bucketName)
        }

        val contentType = file.contentType
        val (fileStat, fileExist) = getFileStat(bucketName, fileName)
        if (!fileExist) {
            minioClient.putObject(bucketName, fileName, file.inputStream, contentType)
            return fileName
        }

        val newFileName = generateNewName(fileName)
        minioClient.putObject(bucketName, newFileName, file.inputStream, contentType)

        val newFileStat = minioClient.statObject(bucketName, newFileName)
        if (newFileStat.etag() == fileStat?.etag()) {
            minioClient.removeObject(bucketName, newFileName)
            return fileName
        }

        return newFileName
    }
}