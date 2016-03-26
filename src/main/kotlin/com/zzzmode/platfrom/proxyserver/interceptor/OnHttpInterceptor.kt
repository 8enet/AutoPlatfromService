package com.zzzmode.platfrom.proxyserver.interceptor

import io.netty.handler.codec.http.HttpRequest
import io.netty.handler.codec.http.HttpResponse
import net.lightbody.bmp.util.HttpMessageContents
import net.lightbody.bmp.util.HttpMessageInfo

/**
 * http proxy 拦截器
 * Created by zl on 16/3/26.
 */
interface OnHttpInterceptor {

    /**
     * 判断是否符合拦截要求,根据需求实现规则匹配
     */
    fun isMatch(httpInfo: HttpMessageInfo):Boolean;

    /**
     * 符合拦截的request
     */
    fun onRequest(request: HttpRequest, contents: HttpMessageContents, messageInfo:ProxyHttpMessageInfo):HttpResponse?;

    /**
     * 符合拦截的response
     */
    fun onResponse(response: HttpResponse, contents: HttpMessageContents, messageInfo:ProxyHttpMessageInfo);
}