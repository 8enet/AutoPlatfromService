package com.zzzmode.platfrom.config

import com.zzzmode.platfrom.proxyserver.EnableHttpProxy
import com.zzzmode.platfrom.proxyserver.HttpProxyAutoConfiguration
import com.zzzmode.platfrom.proxyserver.HttpProxyConfigurer
import com.zzzmode.platfrom.redis.IRedisPublisher
import net.lightbody.bmp.BrowserMobProxy
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration

@Configuration
@EnableHttpProxy
open class HttpProxyConfig : HttpProxyConfigurer {


    @Autowired
    private var redisPublisher : IRedisPublisher?=null

    override fun onConfiguration(proxy: BrowserMobProxy?) {

        //添加过滤器
        proxy?.addRequestFilter {
            httpRequest, httpMessageContents, httpMessageInfo ->


            redisPublisher?.publish("pubsub:queue",httpRequest.uri)

            null
        }

        proxy?.start()

    }



    companion object {

        private val logger = LoggerFactory.getLogger(HttpProxyAutoConfiguration::class.java)
    }

}