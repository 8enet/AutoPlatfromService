package com.zzzmode.platfrom.config.http

import com.zzzmode.platfrom.proxyserver.HttpInterceptor
import com.zzzmode.platfrom.proxyserver.interceptor.OnHttpInterceptor
import com.zzzmode.platfrom.proxyserver.interceptor.ProxyHttpMessageInfo
import io.netty.handler.codec.http.HttpRequest
import io.netty.handler.codec.http.HttpResponse
import net.lightbody.bmp.util.HttpMessageContents
import net.lightbody.bmp.util.HttpMessageInfo
import org.slf4j.LoggerFactory

/**
 * Created by zl on 16/3/26.
 */
@HttpInterceptor
open class HttpImageInterceptor : OnHttpInterceptor {

    companion object{
        private val logger=LoggerFactory.getLogger(HttpImageInterceptor::class.java)
    }

    override fun isMatch(httpInfo: HttpMessageInfo): Boolean {

        return httpInfo.originalUrl.endsWith(".png")
    }

    override fun onRequest(request: HttpRequest, contents: HttpMessageContents, messageInfo: ProxyHttpMessageInfo): HttpResponse? {
        return null
    }

    override fun onResponse(response: HttpResponse, contents: HttpMessageContents, messageInfo: ProxyHttpMessageInfo) {

        logger.debug("img "+response.status+"   "+contents.binaryContents.size+"  "+messageInfo.proxyPort)
    }

    override fun toString(): String{
        return "HttpImageInterceptor()"
    }


}