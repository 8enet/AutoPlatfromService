package com.zzzmode.platfrom.controller

import com.zzzmode.platfrom.bean.ServiceHandleFail
import com.zzzmode.platfrom.exception.PlatfromServiceException
import org.springframework.web.bind.annotation.ExceptionHandler


abstract class BaseController constructor(){

    @ExceptionHandler(PlatfromServiceException::class)
    open fun exception(e: PlatfromServiceException): ServiceHandleFail {
        return ServiceHandleFail(e)
    }


    open fun throwException(msg : String?,code: Int){
        throw PlatfromServiceException(msg,code)
    }
}