package com.zzzmode.platfrom.controller

import com.zzzmode.platfrom.bean.IPAddress
import com.zzzmode.platfrom.bean.MobileNumberAddress
import com.zzzmode.platfrom.exception.PlatfromServiceException
import com.zzzmode.platfrom.services.ToolsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

/**
 * Created by zl on 16/3/6.
 */
@RestController
@ComponentScan
@RequestMapping("/query")
class QueryController :BaseController(){

    @Autowired
    var toolsService: ToolsService?=null



    /**
     * 查询手机归属地
     */
    @RequestMapping(params = arrayOf("phone"))
    @Throws(PlatfromServiceException::class)
    fun viewPhone(@RequestParam phone: String): MobileNumberAddress? {
        toolsService?.getMobileAddress(phone)?.apply {
            return this;
        }
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

        toolsService?.getIpInfo(cip)?.apply {
            return this
        }

        return null
    }

}