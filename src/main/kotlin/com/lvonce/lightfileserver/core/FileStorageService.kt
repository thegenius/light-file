package com.lvonce.lightfileserver.core

import io.minio.MinioClient
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile

interface FileStorageService {
    fun fetch(bucketName:String, fileName:String): Resource?
    fun save(bucketName: String, file:MultipartFile): String?
}