package com.zzzmode.platfrom.proxyserver

import com.zzzmode.platfrom.bean.HttpProxyProperties
import com.zzzmode.platfrom.proxyserver.interceptor.OnHttpInterceptor
import com.zzzmode.platfrom.proxyserver.interceptor.ProxyHttpMessageInfo
import com.zzzmode.platfrom.util.Utils
import net.lightbody.bmp.BrowserMobProxy
import net.lightbody.bmp.BrowserMobProxyServer
import net.lightbody.bmp.mitm.PemFileCertificateSource
import net.lightbody.bmp.mitm.manager.ImpersonatingMitmManager
import org.slf4j.LoggerFactory

/**
 * Created by zl on 16/3/26.
 */
open  class ProxyServer(val properties:HttpProxyProperties) {
    companion object{
        private val logger=LoggerFactory.getLogger(ProxyServer::class.java)
    }

    private var proxyServer: BrowserMobProxy? = null

    /**
     * 构建代理
     */
    fun newServer(): BrowserMobProxy? {
        val cert = Utils.getFile(properties.x509Path);
        val pem = Utils.getFile(properties.pemPath);

        if(cert != null && pem != null && properties.password != null) {

            //然后将证书加载到
            val existingCertificateSource = PemFileCertificateSource(cert, pem, properties.password);

            val mitmManager = ImpersonatingMitmManager.builder()
                    .rootCertificateSource(existingCertificateSource)
                    .build();

            this.proxyServer = BrowserMobProxyServer(properties.port);
            proxyServer?.setMitmManager(mitmManager)
        }
        return this.proxyServer
    }


    open fun addFilter(interceptores:List<OnHttpInterceptor>?):BrowserMobProxy?{
        proxyServer?.addRequestFilter {
            httpRequest, httpMessageContents, httpMessageInfo ->

            interceptores?.forEach {
                if (it.isMatch(httpMessageInfo)) {
                    it.onRequest(httpRequest, httpMessageContents, ProxyHttpMessageInfo(httpMessageInfo, proxyServer!!.port))
                }
            }
            null
        }

        proxyServer?.addResponseFilter { httpResponse, httpMessageContents, httpMessageInfo ->

            interceptores?.forEach {
                if (it.isMatch(httpMessageInfo)) {
                    it.onResponse(httpResponse, httpMessageContents, ProxyHttpMessageInfo(httpMessageInfo,proxyServer!!.port))
                }
            }

        }
        return this.proxyServer
    }

    /**
     * 启动
     */
    fun start(){
        proxyServer?.apply {
            this.start()
            logger.debug("start proxy server : "+proxyServer?.port)
        }
    }

    /**
     * 关闭
     */
    fun stop(){
        proxyServer?.apply {
            this.stop()
            logger.debug("stop proxy server : "+proxyServer?.port)
        }

    }
}