package com.zzzmode.platfrom

import com.zzzmode.platfrom.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent

@SpringBootApplication
open class AppPlatfromServiceApplication : ApplicationListener<ContextRefreshedEvent>{

    @Autowired
    val userService: UserService?=null

    override fun onApplicationEvent(event: ContextRefreshedEvent?) {
        //可选,启动时生成一个用户
        userService?.loadUser()
    }

}


fun main(args: Array<String>) {
    SpringApplication.run(AppPlatfromServiceApplication::class.java, *args)
}
