package com.zzzmode.platfrom.controller

import com.zzzmode.platfrom.services.Yma0SMSPlatfromServices
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Created by zl on 16/2/17.
 */
@RestController
@ComponentScan
@RequestMapping("/sms")
class SMSPlatfromController @Autowired constructor(val yma0SMSPlatfromServices: Yma0SMSPlatfromServices) :BaseController(){


    @RequestMapping(value = "/token", method = arrayOf(RequestMethod.GET))
    fun token(): String? {
        yma0SMSPlatfromServices.getToken()?.apply {
            return this
        }
        throwException("get token error",2)
        return null
    }

    @RequestMapping(value = "/userinfo", method = arrayOf(RequestMethod.GET))
    fun userinfo(): String? {
        yma0SMSPlatfromServices.userInfo?.apply {
            return this
        }
        throwException("get userinfo error",3)
        return null
    }

}
