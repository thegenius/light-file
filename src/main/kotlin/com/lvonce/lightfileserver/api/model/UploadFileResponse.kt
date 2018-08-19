package com.lvonce.lightfileserver.api.model

data class UploadFileResponse (
        var fileName:String,
        var fileUri:String,
        var fileType: String,
        var fileSize: Long
)