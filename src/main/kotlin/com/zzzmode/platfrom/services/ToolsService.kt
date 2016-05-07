package com.zzzmode.platfrom.services

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.zzzmode.platfrom.bean.IPAddress
import com.zzzmode.platfrom.bean.MobileNumberAddress
import com.zzzmode.platfrom.config.ThirdPartyApiConfigure
import com.zzzmode.platfrom.config.ThirdPartyApiConfigure.ApiServerAddress
import com.zzzmode.platfrom.dto.VirtualUser
import com.zzzmode.platfrom.exception.PlatfromServiceException
import com.zzzmode.platfrom.http.HttpRequestClient
import com.zzzmode.platfrom.http.response.*
import com.zzzmode.platfrom.util.JsonKit
import com.zzzmode.platfrom.util.newTemporaryExecutor
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.Request
import org.jsoup.Jsoup
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit

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

    @Autowired
    private val apiConfigure:ThirdPartyApiConfigure?=null



    /********      ip地址    **/

    /**
     * 查询ip地址
     */
    fun getIpInfo(ip:String) : IPAddress?{
        if(!isIpAddress(ip)){
            throw PlatfromServiceException("ip address illegal!",2)
        }

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
                .url(ApiServerAddress.taobao_ip_address_api+ip)
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
                .url(ApiServerAddress.baidu_ip_address_api+ip)
                .addHeader("apikey", apiConfigure?.baiduApiKeyHandler?.getKey())
                .build()

        HttpRequestClient.request(request)?.apply {
            return JsonKit.gson.fromJson(this, IPAddressBaiduResp::class.java)?.ipAddress
        }
        return null
    }








    /**  手机号归属地查询服务 **/



    /**
     * 查询手机号归属地
     */
    fun getMobileAddress(phone: String): MobileNumberAddress? {
        if(!isPhoneNumber(phone)){
            throw PlatfromServiceException("moblie phone number illegal !",1)
        }

        queryBybd(phone)?.apply {
            if(!"-".equals(city)){
                return this
            }

        }

        queryByTenpay(phone)?.apply {
            return this
        }

//        queryBypp(phone)?.apply {
//            return this
//        }
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
        if (apiConfigure?.baiduApiKeyHandler?.getKey() == null) {
            throw RuntimeException("apikey may be not null !!!")
        }

        val request = Request.Builder().get().url(ApiServerAddress.baidu_mobile_number_address_api+ phone).addHeader("apikey", apiConfigure?.baiduApiKeyHandler?.getKey()).build()

        HttpRequestClient.request(request)?.apply {
            return JsonKit.gson.fromJson(this, MobileNumberAddressResp::class.java)?.mobileNumberAddress
        }

        return null
    }

    private fun queryByTenpay(phone: String):MobileNumberAddress?{
        val request = Request.Builder().get().url(ApiServerAddress.tenpay_mobile_number_address_api+phone).build()

        HttpRequestClient.request(request,charset="gb2312")?.apply {
            try {
                val mapper= XmlMapper()
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false)
                return mapper.readValue(this,MobileNumberAddress::class.java)
            } catch(e: Exception) {
            }
        }
        return null
    }


    /**  真实地址生成 **/



    /**
     * 随机生成真实地址
     */
    fun getAddress(province:String?,city:String?):String{

        getAddress_Baidu(province,city)?.apply {
            return this
        }

        getAddress_amap(province,city)?.apply {
            return this
        }

        return ""
    }

    //百度地图地址提取
    private fun getAddress_Baidu(province:String?,city:String?,retry:Int=5):String?{
        if(apiConfigure?.baiduMapAkHandler?.getKey() == null){
            logger.error("baidu map api ak is null !")
            return null
        }

        if(retry == 0){
            logger.warn("getAddress_Baidu fail,province:${province},city:${city},too many times to retry")
            return null
        }

        var randomAD=randomAddress()
        val url = HttpUrl.parse(ApiServerAddress.baidu_map_api).newBuilder()
                .addQueryParameter("ak", apiConfigure?.baiduMapAkHandler?.getKey())
                .addQueryParameter("city_limit", "true")
                .addQueryParameter("output", "json")
                .addQueryParameter("region", if(StringUtils.isEmpty(city)) province else city )
                .addQueryParameter("query", randomAD)
                .build()
        val request = Request.Builder().get().url(url).build()

        HttpRequestClient.request(request)?.apply {
            JsonKit.gson.fromJson(this, BaiduPlaceResp::class.java)?.apply {
                if(status == 4 || status >= 300){
                    apiConfigure?.baiduMapAkHandler?.invalidKey()
                    //当前key配额用完了
                    logger.error("please change baidu map apikey!")
                }

                addressDatas?.run {
                    forEach {
                        it.address?.run {
                            if(contains(randomAD)){
                                if(endsWith("附近")){
                                    return this+it.name
                                }
                                return this
                            }
                        }
                    }
                }
            }
        }
        return getAddress_Baidu(province,city,retry-1)
    }


    //高德地图地址提取
    private fun getAddress_amap(province:String?,city:String?,retry:Int=5):String?{
        if(apiConfigure?.amapApiKeyHandler?.getKey() == null){
            logger.error("amap  api ak is null !")
            return null
        }

        if(retry == 0){
            logger.warn("getAddress_amap fail,province:${province},city:${city},too many times to retry")
            return null
        }

        var randomAD=randomAddress()
        val url = HttpUrl.parse(ApiServerAddress.amap_map_api).newBuilder()
                .addQueryParameter("key", apiConfigure?.amapApiKeyHandler?.getKey())
                .addQueryParameter("city_limit", "true")
                .addQueryParameter("output", "json")
                .addQueryParameter("city", if(StringUtils.isEmpty(city)) province else city )
                .addQueryParameter("keywords", randomAD)
                .build()
        val request = Request.Builder().get().url(url).build()
        HttpRequestClient.request(request)?.apply {
            JsonKit.gson.fromJson(this, AmapPlcaeResp::class.java)?.apply {
                if("10003".equals(infocode)){
                    apiConfigure?.amapApiKeyHandler?.invalidKey()
                    //当前key配额用完了
                    logger.error("please change baidu map apikey!")
                }

                addressDatas?.run {
                    forEach {
                        it.name?.run {
                            if(contains(randomAD)){
                                return  excludeRepeatName(it.pname,it.cityname,it.adname,this)
                            }
                        }

                        it.address?.run {
                            if(contains(randomAD)){

                                return  excludeRepeatName(it.pname,it.cityname,it.adname,this)
                            }
                        }
                    }
                }
            }
        }
        return getAddress_amap(province,city,retry-1)
    }

    private fun excludeRepeatName(p:String?,c:String?,ad:String?,address:String?):String?{
        if(p.isNullOrEmpty()){
            return p+c+ad+address;
        }

        c?.apply {
            if(contains(p!!)){
                return p+ad+address
            }else{
                return p+c+ad+address;
            }
        }

        return p+c+ad+address;
    }


    //随机地址
    // 规则 {[1-6]0[1-4]室}  {[100-600]号}
    private fun randomAddress():String{
        when(RANDOM.nextInt(10)){
            in 0..2 -> return "${RANDOM.nextInt(6)+1}0${RANDOM.nextInt(3)+1}室"
            in 3..8 -> return "${RANDOM.nextInt(300)+100}号"
            else -> return "${RANDOM.nextInt(500)+100}号"
        }
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

        stepChecks(findProxys_66ip(user.city))?.apply {
            return@getProxy this
        }

        stepChecks(findProxys_66ip(user.province))?.apply {
            return@getProxy this
        }

        stepChecks(findProxys_66ip(null))?.apply {
            return@getProxy this
        }
        logger.warn("not found available proxy server !")
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

    private fun stepChecks(inets:List<InetSocketAddress>?):InetSocketAddress?{
        inets?.apply {
            val callables=ArrayList<Callable<InetSocketAddress>>()
            forEach {
                callables.add(
                        Callable<InetSocketAddress> {
                            if(checkChainedProxy(it)){
                                return@Callable  it
                            }else{
                                throw  Exception("proxy connection error !")
                            }

                        }
                )
            }
            var executor=newTemporaryExecutor("tools-check-proxy",size)
            try{
                logger.info("check proxy siez: $size")
                executor?.invokeAny(callables)?.apply {
                    return this
                }
            }catch(e:Throwable){
                //e.printStackTrace()
            }finally{
                 executor!!.shutdownNow()
            }
        }
        return null
    }

    private fun proxyContent(inetSocketAddress: InetSocketAddress):String?{
        val client=HttpRequestClient.getDefaultClient().
                newBuilder()
                .proxy(Proxy(Proxy.Type.HTTP,inetSocketAddress))
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build()

        val request=Request.Builder().url("http://ip.cn").addHeader("User-Agent","curl/7.43.0").build()

        return HttpRequestClient.request(request,client)
    }

}