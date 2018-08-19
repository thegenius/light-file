package com.lvonce.lightfileserver.general

import org.springframework.stereotype.Component

@Component
object IdGenerator {
    private val snowFlake = SnowFlake(23, 23)
    fun nextId(): Long = snowFlake.nextId()
}
