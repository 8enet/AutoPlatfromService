package com.zzzmode.platfrom.services

import com.zzzmode.platfrom.JsonKit
import com.zzzmode.platfrom.bean.MobileNumberAddress
import com.zzzmode.platfrom.http.HttpRequestClient
import com.zzzmode.platfrom.http.response.MobileNumberAddressResp
import okhttp3.Request
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

/**
 * 手机号归属地查询
 * Created by zl on 16/2/16.
 */
@Service
open class MobileNumberAddressService {

    @Value("\${apikey}")
    val apikey: String ? = null

    @Value("\${mobile_number_address_api}")
    val apiServer: String ? = null

    open  fun queryAddress(phone: String): MobileNumberAddress? {
        if (apikey == null) {
            throw RuntimeException("apikey may be not null !!!")
        }

        val request = Request.Builder().get().url(apiServer?.format(phone)).addHeader("apikey", apikey).build()

        HttpRequestClient.request(request)?.apply {
            return JsonKit.gson.fromJson(this, MobileNumberAddressResp::class.java)?.mobileNumberAddress
        }

        return null
    }


    fun isPhoneNumber(phoneNumber: String?): Boolean {
        return phoneNumber?.length == 11 && phoneNumber!!.matches("^\\d{11}$".toRegex())
    }
}
