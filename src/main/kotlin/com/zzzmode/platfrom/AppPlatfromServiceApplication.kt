package com.zzzmode.platfrom

import com.zzzmode.platfrom.dao.repository.SequenceDao
import com.zzzmode.platfrom.dao.repository.UserRepository
import com.zzzmode.platfrom.services.UserService
import io.undertow.Undertow
import io.undertow.UndertowOptions
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.embedded.undertow.UndertowBuilderCustomizer
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

    override fun onApplicationEvent(event: ContextRefreshedEvent?) {
        logger.info("AppPlatfromService start success !")
        sequenceDao?.initSequence()
    }


    /**
     * enable http/2.0
     * {@see http://undertow.io/blog/2015/03/26/HTTP2-In-Wildfly.html}
     * {@see http://www.eclipse.org/jetty/documentation/current/alpn-chapter.html}
     */
    @Bean
    open fun  embeddedServletContainerFactory():UndertowEmbeddedServletContainerFactory {
        val factory = UndertowEmbeddedServletContainerFactory();
        val custom=object : UndertowBuilderCustomizer{
            override fun customize(builder: Undertow.Builder?) {
                builder?.setServerOption(UndertowOptions.ENABLE_HTTP2, true)
            }
        }
        factory.addBuilderCustomizers(custom)
        return factory;
    }


}


fun main(args: Array<String>) {
    SpringApplication.run(AppPlatfromServiceApplication::class.java, *args)
}
