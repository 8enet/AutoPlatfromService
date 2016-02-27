package com.zzzmode.platfrom.config

import com.zzzmode.platfrom.proxyserver.EnableHttpProxy
import com.zzzmode.platfrom.proxyserver.HttpProxyAutoConfiguration
import com.zzzmode.platfrom.proxyserver.HttpProxyConfigurer
import net.lightbody.bmp.BrowserMobProxy
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration

@Configuration
@EnableHttpProxy
open class HttpProxyConfig : HttpProxyConfigurer {

    override fun onConfiguration(proxy: BrowserMobProxy) {

        //添加过滤器
        proxy.addRequestFilter {
            httpRequest, httpMessageContents, httpMessageInfo ->

            logger.debug(httpRequest.uri)

            null
        }

    }



    companion object {

        private val logger = LoggerFactory.getLogger(HttpProxyAutoConfiguration::class.java)
    }

}