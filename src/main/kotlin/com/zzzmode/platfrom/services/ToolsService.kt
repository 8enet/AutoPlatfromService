package com.zzzmode.platfrom.services

import com.zzzmode.platfrom.util.JsonKit
import com.zzzmode.platfrom.bean.IPAddress
import com.zzzmode.platfrom.bean.MobileNumberAddress
import com.zzzmode.platfrom.dto.VirtualUser
import com.zzzmode.platfrom.http.HttpRequestClient
import com.zzzmode.platfrom.http.response.BaiduPlaceResp
import com.zzzmode.platfrom.http.response.IPAddressBaiduResp
import com.zzzmode.platfrom.http.response.IPAddressTaobaoResp
import com.zzzmode.platfrom.http.response.MobileNumberAddressResp
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.Request
import org.jsoup.Jsoup
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.*

/**
 * Created by zl on 16/3/6.
 */
@Service
class ToolsService{
    companion object {

        private val logger = LoggerFactory.getLogger(ToolsService::class.java)

        private val IP_REGX="^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$".toRegex()


        private val PWD_CHARS="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890@"

        private val RANDOM=Random()
    }


    /********      ip地址    **/

    @Value("\${apikey}")
    val apikey: String ? = null

    @Value("\${ip_address_api_taobao}")
    val apiTBServer: String ? = null

    @Value("\${ip_address_api_baidu}")
    val apiBDServer: String ? = null


    /**
     * 查询ip地址
     */
    fun getIpInfo(ip:String) : IPAddress?{
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

    /**
     * 简单判断ip地址
     */
    fun isIpAddress(ip: String?):Boolean{

        ip?.apply {
            return  IP_REGX.matches(this)
        }
        return false
    }

    //淘宝接口
    private fun getIpTaobao(ip:String): IPAddress?{

        val request = Request.Builder().get()
                .url(apiTBServer?.format(ip))
                .build()

        HttpRequestClient.request(request)?.apply {
            return JsonKit.gson.fromJson(this, IPAddressTaobaoResp::class.java)?.ipAddress
        }
        return null
    }

    //百度接口
    private fun getIpBaidu(ip:String): IPAddress?{
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








    /**  手机号归属地查询服务 **/


    @Value("\${mobile_number_address_api}")
    val apiServer: String ? = null

    @Value("\${mobile_number_address_api_paipai}")
    val paipaiServer: String? = null

    /**
     * 查询手机号归属地
     */
    fun getMobileAddress(phone: String): MobileNumberAddress? {
        queryBybd(phone)?.apply {
            return this
        }

        queryBypp(phone)?.apply {
            return this
        }
        return null
    }

    /**
     * 简单校验手机号
     */
    fun isPhoneNumber(phoneNumber: String?): Boolean {
        return phoneNumber?.length == 11 && phoneNumber!!.matches("^\\d{11}$".toRegex())
    }

    //百度接口
    private fun queryBybd(phone: String): MobileNumberAddress? {
        if (apikey == null) {
            throw RuntimeException("apikey may be not null !!!")
        }

        val request = Request.Builder().get().url(apiServer?.format(phone)).addHeader("apikey", apikey).build()

        HttpRequestClient.request(request)?.apply {
            return JsonKit.gson.fromJson(this, MobileNumberAddressResp::class.java)?.mobileNumberAddress
        }

        return null
    }

    //拍拍接口
    private fun queryBypp(phone: String): MobileNumberAddress? {
        val request = Request.Builder().get().url(paipaiServer?.format(phone)).build()

        HttpRequestClient.request(request)?.apply {

            var st: Int? = indexOf('(')
            var et = lastIndexOf(')')
            if (st != -1 && et != -1) {
                val json = substring(st!! + 1, et).replace('\'', '"')

                return JsonKit.gson.fromJson(json, MobileNumberAddress::class.java)
            }

        }
        return null
    }


    /**  真实地址生成 **/

    @Value("\${address_api_server}")
    val addressSearchServer:String?=null

    @Value("\${baidu_ak}")
    val baidu_ak:String?=null

    /**
     * 随机生成真实地址
     */
    fun getAddress(province:String,city:String):String{
        if(baidu_ak == null){
            logger.error("baidu ditu api ak is null !")
            return ""
        }
        val url = HttpUrl.parse(addressSearchServer).newBuilder()
                .addQueryParameter("ak", baidu_ak)
                .addQueryParameter("scope", "1")
                .addQueryParameter("page_size", "5")
                .addQueryParameter("output", "json")
                .addQueryParameter("region", province)
                .addQueryParameter("q", city+randomRoom())
                .build()
        val request = Request.Builder()
                .get()
                .url(url)
                .build()

        HttpRequestClient.request(request)?.apply {
            JsonKit.gson.fromJson(this, BaiduPlaceResp::class.java)?.addressDatas?.apply {
                if(!isEmpty()){
                    forEach {
                        t->
                        t.address?.apply {
                            if(this.length > 6){
                                return this
                            }
                        }
                    }
                }
            }
        }
        return ""
    }

    //随机房间
    private fun randomRoom():String{
        val  random= Random();
        return "${random.nextInt(6)+1}0${random.nextInt(3)+1}室"
    }



    /** 用户名/密码 生成 **/
    data class Result(val username: String, val pwd: String)
    fun getUsernameAndPwd():Result{
        val name=StringBuilder("lshuo1984")
        val pwd=StringBuilder()

        val p=RANDOM.nextInt(3)+8

        for(i in 0..p){
            pwd.append(PWD_CHARS[RANDOM.nextInt(PWD_CHARS.length)])
        }

        return Result(name.toString(),pwd.toString())
    }


    /**
     *
     */
    fun findProxys_66ip(city: String?):List<InetSocketAddress>?{
        var sbx=""
        if(!StringUtils.isEmpty(city)) {
            HttpRequestClient.request(Request.Builder()
                    .url("http://www.66ip.cn/urlencode.php")
                    .post(FormBody.Builder().add("text", city).add("type", "").build())
                    .addHeader("Accept-Language", "zh-CN,zh;q=0.8")
                    .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.108 Safari/537.36")
                    .build())?.apply {

                sbx=this.split("-")[0]

            }
        }

        HttpRequestClient.request(Request.Builder()
                .url(HttpUrl.parse("http://www.66ip.cn/mo.php?&tqsl=10&port=&export=&ktip=&sxa=")
                .newBuilder()
                .addEncodedQueryParameter("sxb",sbx)
                .build()
                )
                .build())?.apply {

            val items= Jsoup.parse(this).body().text().split(" ")
            val proxys=ArrayList<InetSocketAddress>()
            for(item in items){
                parse2Inet(item)?.apply {
                    proxys.add(this)
                }
            }
            return@findProxys_66ip proxys
        }
        return null
    }

    /**
     * 转换
     */
    private fun parse2Inet(str:String?):InetSocketAddress?{
        if(!StringUtils.isEmpty(str)){
            str?.split(":")?.apply {
                val host=this[0]
                val port=this[1]
                if(!StringUtils.isEmpty(host) && !StringUtils.isEmpty(port)){
                    try{
                        return@parse2Inet InetSocketAddress(host,Integer.parseInt(port))
                    }catch(e:Exception){
                        e.printStackTrace()
                    }
                }
            }
        }
        return null
    }

    /** 代理地址 */

    fun getProxy(user:VirtualUser): InetSocketAddress? {

        findProxys_66ip(user.city)?.forEach {
            if (checkChainedProxy(it)) {
                return@getProxy it
            }
        }

        findProxys_66ip(user.province)?.forEach {
            if (checkChainedProxy(it)) {
                return@getProxy it
            }
        }
        findProxys_66ip(null)?.forEach {
            if (checkChainedProxy(it)) {
                return@getProxy it
            }
        }
        logger.warn("not found proxy server !")
        return null
    }

    /**
     * 检测代理是否有效
     */
    fun checkChainedProxy(inetSocketAddress: InetSocketAddress):Boolean{
        proxyContent(inetSocketAddress)?.apply {
            //logger.debug("checkChainedProxy --> "+this+"   "+inetSocketAddress)
            return@checkChainedProxy this.contains(inetSocketAddress.hostString)
        }
        return false
    }

    private fun proxyContent(inetSocketAddress: InetSocketAddress):String?{
        val client=HttpRequestClient.getDefaultClient().newBuilder().proxy(Proxy(Proxy.Type.HTTP,inetSocketAddress)).build()

        val request=Request.Builder().url("http://ip.cn").addHeader("User-Agent","curl/7.43.0").build()

        return HttpRequestClient.request(request,client)
    }

}