package com.lvonce.lightfileserver

import com.lvonce.lightfileserver.core.FileServiceProperties
import com.lvonce.lightfileserver.general.CrossOriginFilterProperties

import org.springframework.boot.runApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class LightFileServerApplication

fun main(args: Array<String>) {
    FileServiceProperties.readFromArgs(*args)
    CrossOriginFilterProperties.readFromArgs(*args)
    runApplication<LightFileServerApplication>(*args)
}
