package com.lvonce.lightfileserver.general

import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import com.lvonce.lightfileserver.core.FileServiceProperties

class CrossOriginFilterProperties {
    companion object {
        var instance: CrossOriginFilterProperties? = null
        fun readFromArgs(vararg args: String) {
            val crossArgs = filterArgs(*args)
            val crossOriginFilterProperties = CrossOriginFilterProperties()
            JCommander.newBuilder().addObject(crossOriginFilterProperties).build().parse(*crossArgs)
            CrossOriginFilterProperties.instance = crossOriginFilterProperties
        }

        private fun filterArgs(vararg args: String) : Array<String> {
            val result = ArrayList<String>()
            for (i in 0 until args.size) {
                if (args[i] in arrayOf("--cross")) {
                    result.add(args[i])
                }
            }
            return result.toTypedArray()
        }
    }

    @Parameter(names = ["--cross"], description = "enable/disable cross domain")
    var enable:Boolean = false
}