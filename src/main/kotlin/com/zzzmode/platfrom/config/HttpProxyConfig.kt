package com.zzzmode.platfrom.config

import com.zzzmode.platfrom.proxyserver.EnableHttpProxy
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

            logger.debug(httpRequest.uri)

            redisPublisher?.publish("pubsub:queue",httpRequest.uri)

            null
        }


//        if (proxy is BrowserMobProxyServer){
//            proxy.setChainedProxyManager { httpRequest, queue ->
//
//                val chainedProxy=object : ChainedProxyAdapter(){
//                    override fun getChainedProxyAddress(): InetSocketAddress? {
//                        return super.getChainedProxyAddress()
//                    }
//                }
//                queue.add(chainedProxy)
//
//            }
//        }

        proxy?.start()
    }



    companion object {

        private val logger = LoggerFactory.getLogger(HttpProxyConfig::class.java)
    }

}