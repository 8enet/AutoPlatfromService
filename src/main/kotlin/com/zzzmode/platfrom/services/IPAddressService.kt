package com.zzzmode.platfrom.services

import com.zzzmode.platfrom.JsonKit
import com.zzzmode.platfrom.bean.IPAddress
import com.zzzmode.platfrom.http.HttpRequestClient
import com.zzzmode.platfrom.http.response.IPAddressBaiduResp
import com.zzzmode.platfrom.http.response.IPAddressTaobaoResp
import okhttp3.Request
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

/**
 * Created by zl on 16/3/6.
 */
@Service
open class IPAddressService {
    companion object {

        private val logger = LoggerFactory.getLogger(IPAddressService::class.java)

        private val IP_REGX="^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$".toRegex()
    }

    @Value("\${apikey}")
    val apikey: String ? = null

    @Value("\${ip_address_api_taobao}")
    val apiTBServer: String ? = null

    @Value("\${ip_address_api_baidu}")
    val apiBDServer: String ? = null


    /**
     * 查询ip地址
     */
    open fun getIpInfo(ip:String) : IPAddress?{
        //先用淘宝
        getIpTaobao(ip)?.apply {
            return this
        }
        //如果失败则使用百度
        getIpBaidu(ip)?.apply {
            return this
        }
        return null
    }

    open fun isIpAddress(ip: String?):Boolean{
        if(ip == null){
            return false
        }
        return IP_REGX.matches(ip)
    }

    //淘宝接口
    private fun getIpTaobao(ip:String):IPAddress?{

        val request = Request.Builder().get()
                .url(apiTBServer?.format(ip))
                .build()

        HttpRequestClient.request(request)?.apply {
            return JsonKit.gson.fromJson(this, IPAddressTaobaoResp::class.java)?.ipAddress
        }
        return null
    }

    //百度接口
    private fun getIpBaidu(ip:String):IPAddress?{
        val request = Request.Builder()
                .get()
                .url(apiBDServer?.format(ip))
                .addHeader("apikey", apikey)
                .build()

        HttpRequestClient.request(request)?.apply {
            return JsonKit.gson.fromJson(this, IPAddressBaiduResp::class.java)?.ipAddress
        }
        return null
    }




}