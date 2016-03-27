package com.zzzmode.platfrom.proxyserver

import com.zzzmode.platfrom.bean.HttpProxyProperties
import com.zzzmode.platfrom.proxyserver.interceptor.OnHttpInterceptor
import com.zzzmode.platfrom.proxyserver.interceptor.ProxyHttpMessageInfo
import com.zzzmode.platfrom.util.Utils
import net.lightbody.bmp.BrowserMobProxy
import net.lightbody.bmp.BrowserMobProxyServer
import net.lightbody.bmp.mitm.PemFileCertificateSource
import net.lightbody.bmp.mitm.manager.ImpersonatingMitmManager
import org.littleshoot.proxy.ChainedProxyAdapter
import org.slf4j.LoggerFactory
import java.net.InetSocketAddress

/**
 * Created by zl on 16/3/26.
 */
class ProxyServer{
    companion object{
        private val logger=LoggerFactory.getLogger(ProxyServer::class.java)
    }

    private var proxyServer: BrowserMobProxyServer? = null

    /**
     * 构建代理
     */
    fun newServer(properties:HttpProxyProperties): BrowserMobProxy? {
        val cert = Utils.getFile(properties.x509Path);
        val pem = Utils.getFile(properties.pemPath);

        this.proxyServer = BrowserMobProxyServer(properties.port);

        if(cert != null && pem != null && properties.password != null) {
            //然后将证书加载到
            try{
                val existingCertificateSource = PemFileCertificateSource(cert, pem, properties.password);
                val mitmManager = ImpersonatingMitmManager.builder()
                        .rootCertificateSource(existingCertificateSource)
                        .build();
                proxyServer?.setMitmManager(mitmManager)
            }catch(e:Exception){
                e.printStackTrace()
            }

        }else{
            logger.warn("Certificate error,unsupport https proxy !")
        }
        return this.proxyServer
    }


    fun addFilter(interceptores:List<OnHttpInterceptor>?):BrowserMobProxy?{
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
     * 添加二级代理
     */
    fun addScendaryProxy(chainedProxyAddress:InetSocketAddress?){

        chainedProxyAddress?.apply {
            logger.debug("addScendaryProxy -> port: ${proxyServer?.port}  ${chainedProxyAddress}")
            proxyServer?.setChainedProxyManager { httpRequest, queue ->

                val chainedProxyAdapter=object : ChainedProxyAdapter() {
                    override fun getChainedProxyAddress(): InetSocketAddress? {
                        return chainedProxyAddress
                    }
                }
                queue.add(chainedProxyAdapter)
                queue.add(ChainedProxyAdapter.FALLBACK_TO_DIRECT_CONNECTION)
            }
        }

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
            if(this is BrowserMobProxyServer){
                filterFactories.clear()
            }
            logger.debug("stop proxy server : "+proxyServer?.port)
        }

    }
}