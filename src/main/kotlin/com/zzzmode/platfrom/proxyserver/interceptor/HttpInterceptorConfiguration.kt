package com.zzzmode.platfrom.proxyserver

import com.zzzmode.platfrom.proxyserver.interceptor.OnHttpInterceptor
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.CollectionUtils
import java.util.*

@Configuration
open  class HttpInterceptorConfiguration{

    companion object {
        private val logger = LoggerFactory.getLogger(HttpInterceptorConfiguration::class.java)
    }

    /**
     * 所有的http代理拦截
     */
    val interceptores = ArrayList<OnHttpInterceptor>()

    @Autowired(required = false)
    fun setInterceptores(list: List<OnHttpInterceptor>) {
        if (!CollectionUtils.isEmpty(list)) {
            this.interceptores.addAll(list)
        }
        logger.debug("setInterceptores ---- "+interceptores)
    }


    @Bean
    open fun getInterceptores():List<OnHttpInterceptor>{
        return interceptores
    }
}