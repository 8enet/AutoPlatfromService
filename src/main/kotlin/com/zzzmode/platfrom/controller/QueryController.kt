package com.zzzmode.platfrom.controller

import com.zzzmode.platfrom.bean.IPAddress
import com.zzzmode.platfrom.bean.MobileNumberAddress
import com.zzzmode.platfrom.exception.PlatfromServiceException
import com.zzzmode.platfrom.services.IPAddressService
import com.zzzmode.platfrom.services.MobileNumberAddressService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.RequestContext
import javax.servlet.http.HttpServletRequest

/**
 * Created by zl on 16/3/6.
 */
@RestController
@ComponentScan
@RequestMapping("/query")
class QueryController :BaseController(){

    @Autowired
    var mobileNumberAddressServices: MobileNumberAddressService?=null

    @Autowired
    var ipAddressService:IPAddressService?=null


    /**
     * 查询手机归属地
     */
    @RequestMapping(params = arrayOf("phone"))
    @Throws(PlatfromServiceException::class)
    fun viewPhone(@RequestParam phone: String): MobileNumberAddress? {
        if (mobileNumberAddressServices?.isPhoneNumber(phone)!!) {

            mobileNumberAddressServices?.queryAddress(phone)?.apply {
                return this;
            }
        }
        throwException("query fail!", 1)

        return null
    }


    /**
     * 查询ip地址
     */
    @RequestMapping(params = arrayOf("ip"))
    @Throws(PlatfromServiceException::class)
    fun viewIP(@RequestParam() ip: String,request:HttpServletRequest): IPAddress? {
        var cip=ip
        if("".equals(cip)){
            cip=request.remoteHost
        }
        if(ipAddressService?.isIpAddress(cip)!!){
            ipAddressService?.getIpInfo(cip)?.apply {
                return this
            }
        }else{
            throwException("不是ip地址", 3)
        }
        return null
    }

}