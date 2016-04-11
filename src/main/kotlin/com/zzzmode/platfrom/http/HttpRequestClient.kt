package com.zzzmode.platfrom.http

import com.zzzmode.platfrom.bean.ObjectWapper
import okhttp3.*
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by zl on 16/2/16.
 */
object HttpRequestClient {
    private val defaultClient : OkHttpClient
    init {
        defaultClient = OkHttpClient.Builder()
                //.proxy(Proxy(Proxy.Type.HTTP, InetSocketAddress("127.0.0.1", 8888)))
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build()

    }

    fun getDefaultClient():OkHttpClient{
        return defaultClient
    }

    private val logger = LoggerFactory.getLogger(HttpRequestClient::class.java)


    fun request(request: Request,httpClient : OkHttpClient=defaultClient): String? {
        var response:Response?=null
        try {
            response=httpClient.newCall(request).execute()

            if(response.isSuccessful){
                return response.body()?.string()
            }
        } catch(e: Exception) {
            try {
                response?.body()?.close()
            } catch(e: Exception) {
            }finally{
                throw e
            }
        }
        //logger.info("Http req --> " + request.url() + " \n resp --> " + wapper.value)
        return null
    }

    fun requestAsync(request: Request,callback: Callback?,httpClient : OkHttpClient=defaultClient){

        httpClient.newCall(request).enqueue(callback)

    }





    @JvmStatic
    fun main(args: Array<String>) {

        val url2 = HttpUrl.Builder().scheme("http").host("api.yma0.com").addPathSegment("http.aspx").addQueryParameter("action", "loginIn").addQueryParameter("uid", "zts689").addQueryParameter("pwd", "zts689123").build()

        println(url2)


        val s = "zts689|72e74905bbc3e6aa"

        val split = s.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        println(Arrays.toString(split))

        val  r=run1()

        r.let {
            println(" let  "+this)
        }
        if(r == null){
            println("r is null "+this)
        }else{
            println(r)
        }

        r?.let {
            println("r  is null ---let  "+r.toString()+"   "+this)
        }

        r?.apply{
            println("r  is null ---apply "+this)
        }


    }
    fun run1() :String?{
        val wapper = ObjectWapper<String?>()
        wapper.value="sdf"
        return wapper.value
    }
}
