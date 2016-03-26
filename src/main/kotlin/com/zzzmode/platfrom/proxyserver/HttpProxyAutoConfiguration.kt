package com.zzzmode.platfrom.proxyserver

import com.zzzmode.platfrom.bean.HttpProxyProperties
import com.zzzmode.platfrom.util.Utils
import net.lightbody.bmp.BrowserMobProxy
import net.lightbody.bmp.BrowserMobProxyServer
import net.lightbody.bmp.mitm.PemFileCertificateSource
import net.lightbody.bmp.mitm.manager.ImpersonatingMitmManager
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.CollectionUtils
import java.net.UnknownHostException
import java.util.*
import javax.annotation.PreDestroy

/**
 * Created by zl on 16/2/27.
 */
@Configuration
@ConditionalOnClass(BrowserMobProxy::class)
@EnableConfigurationProperties(HttpProxyProperties::class)
open class HttpProxyAutoConfiguration {

    private val configurers = ArrayList<HttpProxyConfigurer>()

    @Autowired(required = false)
    private val properties: HttpProxyProperties? = null

    private var proxyServer: BrowserMobProxy? = null

    @PreDestroy
    fun close() {
        proxyServer?.stop()
    }

    @Autowired(required = false)
    fun setConfigurers(configurers: List<HttpProxyConfigurer>) {
        if (!CollectionUtils.isEmpty(configurers)) {
            this.configurers.addAll(configurers)
        }
    }


    @Bean
    @ConditionalOnMissingBean
    @Throws(UnknownHostException::class)
    open fun proxy(): BrowserMobProxy? {
        val cert = Utils.getFile(properties?.x509Path);
        val pem = Utils.getFile(properties?.pemPath);

        if(cert != null && pem != null && properties?.password != null) {

            logger.error(properties?.toString())

            //然后将证书加载到
            val existingCertificateSource = PemFileCertificateSource(cert, pem, properties?.password);

            val mitmManager = ImpersonatingMitmManager.builder()
                    .rootCertificateSource(existingCertificateSource)
                    .build();


            this.proxyServer = BrowserMobProxyServer(properties?.port!!);
            proxyServer?.setMitmManager(mitmManager)


            for (configurer in this.configurers) {
                configurer.onConfiguration(this.proxyServer)
            }
        }

        return this.proxyServer
    }

    private  fun chackProxyProperties():Boolean{
        if(properties != null){
            return (properties.x509Path != null && properties.pemPath != null && properties.password!= null)
        }

        return  false
    }


    companion object {

        private val logger = LoggerFactory.getLogger(HttpProxyAutoConfiguration::class.java)
    }

}