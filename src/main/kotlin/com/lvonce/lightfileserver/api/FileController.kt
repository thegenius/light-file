package com.lvonce.lightfileserver.api
import com.lvonce.lightfileserver.api.model.UploadFileResponse
import com.lvonce.lightfileserver.core.FileStorageService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder


@RestController
class FileController {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(FileController::class.java)
    }

    @Autowired
    private lateinit var fileStorageService: FileStorageService

    @GetMapping(value = ["/file-hello"])
    fun hello():String {
        return "hello file"
    }

    @GetMapping("/file")
    fun getFile(@RequestParam bucketName:String, @RequestParam fileName:String)
        : ResponseEntity<Resource> {
        logger.info("{} - {}", bucketName, fileName)

        val resource:Resource? = fileStorageService.fetch(bucketName, fileName)
        val contentType = MediaType.parseMediaType("application/octet-stream")
        val headersValue = "attachment; filename=\"${fileName}\""
        if (resource == null) {
            return ResponseEntity.badRequest().build()
        }
        val result = ResponseEntity
                .ok()
                .contentType(contentType)
                .header(HttpHeaders.CONTENT_DISPOSITION, headersValue)
                .body(resource)
        return result
    }

    @PostMapping("/file")
    fun postFile(@RequestParam bucketName: String, @RequestParam file: MultipartFile) : ResponseEntity<UploadFileResponse> {
        val fileName = fileStorageService.save(bucketName, file)
        if (fileName.isNullOrBlank())  {
            return ResponseEntity.badRequest().build()
        }

        /*
        val fileDownloadUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/file/") //这个需要个GetMapping对应
                .queryParam("bucketName", bucketName)
                .queryParam("fileName", fileName)
                .toUriString()
        */

        val fileUri = "/file?bucketName=$bucketName&fileName=$fileName"
        var response = UploadFileResponse(
                fileName?:"",
                fileUri,
                file.contentType?:"",
                file.size
        )
        return ResponseEntity(response, HttpStatus.OK)
    }

}