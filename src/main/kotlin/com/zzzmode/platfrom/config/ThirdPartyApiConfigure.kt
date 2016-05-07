package com.zzzmode.platfrom.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.util.concurrent.CopyOnWriteArrayList
import javax.annotation.PostConstruct

/**
 * 第三方api配置
 * Created by zl on 16/5/7.
 */
@Component
class ThirdPartyApiConfigure {

    val baiduApiKeyHandler=KeysHandler("baiduApiKey")

    val baiduMapAkHandler=KeysHandler("baiduMapAk")

    val amapApiKeyHandler=KeysHandler("amapApiKey")


    @PostConstruct
    fun initasasas(){
        println(baiduApiKey)
        setKey(baiduApiKey,baiduApiKeyHandler)
        setKey(baiduMapAK,baiduMapAkHandler)
        setKey(amapApiKey,amapApiKeyHandler)
    }

    @Value("\${apikey}")
    private var baiduApiKey:String?=null


    @Value("\${baidu_ak}")
    private var baiduMapAK:String?=null


    @Value("\${amap_api_key}")
    private var amapApiKey:String?=null


    private fun setKey(value: String?,handler:KeysHandler){
        value?.split(",")?.toList()?.apply {
            handler.addAll(this)
        }
    }

    object  ApiServerAddress{

        /**
         * 百度 手机号归属地查询api
         */
        const  val baidu_mobile_number_address_api="http://apis.baidu.com/apistore/mobilenumber/mobilenumber?phone="

        /**
         * 腾讯财付通 手机号归属地查询api
         */
        const val tenpay_mobile_number_address_api="http://life.tenpay.com/cgi-bin/mobile/MobileQueryAttribution.cgi?chgmobile="

        /**
         * 淘宝 ip地址查询
         */
        const val taobao_ip_address_api="http://ip.taobao.com/service/getIpInfo.php?ip="

        /**
         * 百度 ip地址查询
         */
        const val baidu_ip_address_api="http://apis.baidu.com/apistore/iplookupservice/iplookup?ip="

        /**
         * 百度地图api
         */
        const val baidu_map_api="http://api.map.baidu.com/place/v2/search"

        /**
         * 高德地图api
         */
        const val amap_map_api="http://restapi.amap.com/v3/place/text?parameters"

    }




    interface ApiKey{
        fun getKey():String?
        fun invalidKey()
    }


    class KeysHandler (private val name:String): ApiKey{

        companion object{
            private val logger=LoggerFactory.getLogger(KeysHandler::class.java)
        }

        private val keys=CopyOnWriteArrayList<String>()

        @Volatile
        private  var currKey:String?=null

        override fun getKey(): String? {
            if (currKey==null ) {
                synchronized(keys,{
                    if(!keys.isEmpty()){
                        currKey = keys.get(0)
                    }else{
                        logger.warn("${name} , There is not enough key")
                    }
                })
            }
            return currKey
        }

        @Synchronized
        override fun invalidKey() {
            currKey?.let {
                keys.remove(currKey)
            }
            currKey=null
        }

        internal fun addAll(list: List<String>){
            keys.addAll(list)
        }

    }

}