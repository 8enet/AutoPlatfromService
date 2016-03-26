package com.zzzmode.platfrom.proxyserver.interceptor

import io.netty.handler.codec.http.HttpRequest
import net.lightbody.bmp.util.HttpMessageInfo
import io.netty.channel.ChannelHandlerContext

/**
 * Created by zl on 16/3/26.
 */
open class ProxyHttpMessageInfo: HttpMessageInfo {
    var proxyPort:Int=0
        get

    constructor(originalRequest:HttpRequest, channelHandlerContext: ChannelHandlerContext, isHttps: Boolean ,  url: String , originalUrl: String) : super(originalRequest,channelHandlerContext,isHttps,url,originalUrl){
    }

    constructor(httpInfo:HttpMessageInfo,port:Int):super(httpInfo.originalRequest,httpInfo.channelHandlerContext,httpInfo.isHttps,httpInfo.url,httpInfo.originalUrl){
        proxyPort=port
    }
}