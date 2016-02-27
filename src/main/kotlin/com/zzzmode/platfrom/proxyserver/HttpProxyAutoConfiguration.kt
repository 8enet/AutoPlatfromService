package com.zzzmode.platfrom.proxyserver

import com.zzzmode.platfrom.bean.HttpProxyProperties
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
import java.io.File
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

    @Autowired
    private val properties: HttpProxyProperties? = null

    private var proxyServer: BrowserMobProxy? = null

    @PreDestroy
    fun close() {
        if (this.proxyServer != null) {
            this.proxyServer!!.stop()
        }
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

        logger.error(properties?.toString())

        val cert = File(properties?.x509Path);
        val pem = File(properties?.pemPath);

        //然后将证书加载到
        val existingCertificateSource = PemFileCertificateSource(cert, pem, properties?.password);

        val mitmManager = ImpersonatingMitmManager.builder()
                .rootCertificateSource(existingCertificateSource)
                .build();


        this.proxyServer = BrowserMobProxyServer(properties!!.port);
        proxyServer?.setMitmManager(mitmManager)


        for (configurer in this.configurers) {
            configurer.onConfiguration(this.proxyServer!!)
        }

        proxyServer?.start()

        return this.proxyServer
    }


    companion object {

        private val logger = LoggerFactory.getLogger(HttpProxyAutoConfiguration::class.java)
    }

}