package com.zzzmode.platfrom.config

import com.zzzmode.platfrom.proxyserver.EnableHttpProxy
import com.zzzmode.platfrom.proxyserver.HttpProxyConfigurer
import com.zzzmode.platfrom.proxyserver.interceptor.OnHttpInterceptor
import com.zzzmode.platfrom.redis.IRedisPublisher
import net.lightbody.bmp.BrowserMobProxy
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

/**
 * http代理配置,并在此拦截请求
 */

@EnableHttpProxy
open class HttpProxyConfig : HttpProxyConfigurer {

    companion object {
        private val logger = LoggerFactory.getLogger(HttpProxyConfig::class.java)
    }

    @Autowired
    private var redisPublisher: IRedisPublisher? = null

    @Autowired
    private var interceptores: List<OnHttpInterceptor>? = null

    override fun onConfiguration(proxy: BrowserMobProxy?) {

        //添加过滤器
        proxy?.addRequestFilter {
            httpRequest, httpMessageContents, httpMessageInfo ->

            //redisPublisher?.publish("pubsub:queue",httpRequest.uri)

            interceptores?.forEach {
                if (it.isMatch(httpMessageInfo)) {
                    it.onRequest(httpRequest, httpMessageContents, httpMessageInfo)
                }
            }

            null
        }

        proxy?.addResponseFilter { httpResponse, httpMessageContents, httpMessageInfo ->
            //logger.debug("resp --> "+httpMessageInfo.originalUrl)

            interceptores?.forEach {
                if (it.isMatch(httpMessageInfo)) {
                    it.onResponse(httpResponse, httpMessageContents, httpMessageInfo)
                }
            }

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




}