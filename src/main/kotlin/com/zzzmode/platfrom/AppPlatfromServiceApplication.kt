package com.zzzmode.platfrom

import com.zzzmode.platfrom.config.ServerContainerConfigure
import com.zzzmode.platfrom.dao.repository.SequenceDao
import com.zzzmode.platfrom.dao.repository.UserRepository
import com.zzzmode.platfrom.services.UserService
import io.undertow.Undertow
import io.undertow.UndertowOptions
import io.undertow.servlet.Servlets
import io.undertow.servlet.api.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.embedded.undertow.UndertowBuilderCustomizer
import org.springframework.boot.context.embedded.undertow.UndertowDeploymentInfoCustomizer
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.event.ContextRefreshedEvent

@SpringBootApplication
open class AppPlatfromServiceApplication : ApplicationListener<ContextRefreshedEvent>{

    companion object{
        private val logger=LoggerFactory.getLogger(AppPlatfromServiceApplication::class.java)
    }

    @Autowired
    var userService: UserService?=null

    @Autowired
    val sequenceDao: SequenceDao?=null

    @Autowired
    var serverConfig: ServerContainerConfigure?=null

    override fun onApplicationEvent(event: ContextRefreshedEvent?) {
        logger.info("AppPlatfromService start success !")
        sequenceDao?.initSequence()
    }


    /**
     * enable http/2.0
     * {@see http://undertow.io/blog/2015/03/26/HTTP2-In-Wildfly.html}
     * {@see http://www.eclipse.org/jetty/documentation/current/alpn-chapter.html}
     * {@see http://central.maven.org/maven2/org/mortbay/jetty/alpn/alpn-boot}
     */
    @Bean
    open fun  embeddedServletContainerFactory():UndertowEmbeddedServletContainerFactory {
        val factory = UndertowEmbeddedServletContainerFactory()

        logger.debug("${serverConfig}")
        serverConfig?.http2?.apply {
            if(enabled){
                //启用http/2.0
                factory.addBuilderCustomizers(UndertowBuilderCustomizer {
                    it?.setServerOption(UndertowOptions.ENABLE_HTTP2, true)
                })
            }
        }


        serverConfig?.http?.apply {
            if(enabled){
                //启用http
                factory.addBuilderCustomizers(UndertowBuilderCustomizer({
                    it?.addHttpListener(port, "0.0.0.0")
                }))
            }


            if(redirect2Https){
                //将http请求重定向到https
                factory.addDeploymentInfoCustomizers(UndertowDeploymentInfoCustomizer({
                    it.addSecurityConstraint(
                            SecurityConstraint()
                                    .addWebResourceCollection(WebResourceCollection().addUrlPattern("/*"))
                                    .setTransportGuaranteeType(TransportGuaranteeType.CONFIDENTIAL)
                                    .setEmptyRoleSemantic(SecurityInfo.EmptyRoleSemantic.PERMIT)
                    ).confidentialPortManager = ConfidentialPortManager({ factory.port })
                }))
            }
        }

        return factory;
    }


}


fun main(args: Array<String>) {
    SpringApplication.run(AppPlatfromServiceApplication::class.java, *args)
}
