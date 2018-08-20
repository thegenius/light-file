package com.lvonce.lightfileserver.general


import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

import javax.servlet.*
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class CrossOriginFilter : Filter {
    val properties: CrossOriginFilterProperties? by lazy {
        CrossOriginFilterProperties.instance
    }
    @Throws(IOException::class, ServletException::class)
    override fun doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) {
        if (properties?.enable?:false) {
            val response = res as HttpServletResponse
            val request = req as HttpServletRequest
            response.setHeader("Access-Control-Allow-Origin", "*")
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE")
            response.setHeader("Access-Control-Max-Age", "3600")
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, x-requested-with, authorization")

            if ("OPTIONS".equals(request.method, ignoreCase = true)) {
                response.status = HttpServletResponse.SC_OK
            }
        }
        chain.doFilter(req, res)
    }

    override fun init(filterConfig: FilterConfig) {}

    override fun destroy() {}
}