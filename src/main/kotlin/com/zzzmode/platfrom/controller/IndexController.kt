package com.zzzmode.platfrom.controller

import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import java.text.DateFormat
import java.util.Date

/**
 * Created by zl on 16/2/16.
 */
@RestController
@RequestMapping("/")
class IndexController : BaseController(){

    @RequestMapping("/")
    fun hello(@RequestHeader headers: HttpHeaders): String {
        return "server ok !" + DateFormat.getInstance().format(Date()) + "\n" + headers.getFirst(HttpHeaders.USER_AGENT)
    }
}
