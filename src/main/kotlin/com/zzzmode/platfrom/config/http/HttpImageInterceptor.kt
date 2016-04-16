package com.zzzmode.platfrom.config.http

import com.zzzmode.platfrom.proxyserver.HttpInterceptor
import com.zzzmode.platfrom.proxyserver.interceptor.OnHttpInterceptor
import com.zzzmode.platfrom.proxyserver.interceptor.ProxyHttpMessageInfo
import com.zzzmode.platfrom.services.CaptchaRecognizeService
import io.netty.handler.codec.http.HttpRequest
import io.netty.handler.codec.http.HttpResponse
import net.lightbody.bmp.util.HttpMessageContents
import net.lightbody.bmp.util.HttpMessageInfo
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value

/**
 * Created by zl on 16/3/26.
 */
@HttpInterceptor
open class HttpImageInterceptor : OnHttpInterceptor {

    companion object{
        private val logger=LoggerFactory.getLogger(HttpImageInterceptor::class.java)
    }

    @Value("\${captcha_regx}")
    var imgRule:String?=null


    @Autowired
    var captchaRecognizeService: CaptchaRecognizeService?=null

    override fun isMatch(httpInfo: HttpMessageInfo): Boolean {

        if(!imgRule.isNullOrEmpty()){
            return httpInfo.originalUrl.contains(imgRule!!)
        }


        return false
    }

    override fun onRequest(request: HttpRequest, contents: HttpMessageContents, messageInfo: ProxyHttpMessageInfo): HttpResponse? {
        return null
    }

    override fun onResponse(response: HttpResponse, contents: HttpMessageContents, messageInfo: ProxyHttpMessageInfo) {

        logger.info("response port ${messageInfo.proxyPort} find img: ${messageInfo.originalUrl}  status:${response.status} size:${contents.binaryContents.size}")

        captchaRecognizeService?.onRecvData(contents.binaryContents,messageInfo.proxyPort)
    }

    override fun toString(): String{
        return "HttpImageInterceptor()"
    }


}