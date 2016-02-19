package com.zzzmode.platfrom.controller

import com.zzzmode.platfrom.bean.MobileNumberAddress
import com.zzzmode.platfrom.bean.ServiceHandleFail
import com.zzzmode.platfrom.exception.PlatfromServiceException
import com.zzzmode.platfrom.services.MobileNumberAddressServices
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.web.bind.annotation.*

/**
 * Created by zl on 16/2/16.
 */
@RestController
@ComponentScan
@RequestMapping("/phone")
class MobileNumberController @Autowired constructor(val mobileNumberAddressServices:MobileNumberAddressServices) :BaseController(){


    @RequestMapping(value = "/{phone}", method = arrayOf(RequestMethod.GET))
    @Throws(PlatfromServiceException::class)
    fun view(@PathVariable("phone") phone: String): MobileNumberAddress? {
        if (mobileNumberAddressServices.isPhoneNumber(phone)) {

            mobileNumberAddressServices.queryAddress(phone)?.apply {
                return this;
            }
        }
        throwException("query fail!", 1)

        return null
    }


}
