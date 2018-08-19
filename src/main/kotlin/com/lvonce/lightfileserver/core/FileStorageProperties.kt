package com.lvonce.lightfileserver.core

import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter

class FileServiceProperties {
    companion object {
        var instance: FileServiceProperties? = null
        fun readFromArgs(vararg args: String) {
            val fileArgs = filterArgs(*args)
            val fileServiceProperties = FileServiceProperties()
            JCommander.newBuilder().addObject(fileServiceProperties).build().parse(*fileArgs)
            instance = fileServiceProperties
        }

        private fun filterArgs(vararg args: String) : Array<String> {
            val result = ArrayList<String>()
            for (i in 0 until args.size) {
                if (args[i] in arrayOf("--endpoint", "--access", "--secret")) {
                    result.add(args[i])
                    result.add(args[i + 1])
                }
            }
            return result.toTypedArray()
        }

    }
    @Parameter(names = ["--endpoint"], description = "endpoint of minio")
    var endpoint:String = ""
    @Parameter(names = ["--access"], description = "access key of minio")
    var accessKey:String = ""
    @Parameter(names = ["--secret"], description = "secret key of minio")
    var secretKey:String = ""
}
