package com.zzzmode.platfrom

import com.zzzmode.platfrom.services.UserService
import io.undertow.Undertow
import io.undertow.UndertowOptions
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

    @Autowired
    val userService: UserService?=null

    override fun onApplicationEvent(event: ContextRefreshedEvent?) {
        //可选,启动时生成一个用户
        //userService?.loadUser()
    }


//    @Bean
//    open fun  embeddedServletContainerFactory():UndertowEmbeddedServletContainerFactory {
//        val factory = UndertowEmbeddedServletContainerFactory();
//        val custom=object : UndertowBuilderCustomizer{
//            override fun customize(builder: Undertow.Builder?) {
//                builder?.setServerOption(UndertowOptions.ENABLE_HTTP2, true)
//            }
//        }
//        factory.addBuilderCustomizers(custom)
//        return factory;
//    }


}


fun main(args: Array<String>) {
    SpringApplication.run(AppPlatfromServiceApplication::class.java, *args)
}
