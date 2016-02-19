package com.zzzmode.platfrom.http

import com.google.gson.Gson
import com.zzzmode.platfrom.bean.MobileNumberAddress
import com.zzzmode.platfrom.bean.ObjectWapper
import com.zzzmode.platfrom.http.response.MobileNumberAddressResp
import okhttp3.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.json.GsonJsonParser
import org.springframework.util.SocketUtils

import java.io.IOException
import java.lang.ref.SoftReference
import java.util.Arrays
import java.util.concurrent.BrokenBarrierException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.CyclicBarrier
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock

/**
 * Created by zl on 16/2/16.
 */
object HttpRequestClient {

    private val logger = LoggerFactory.getLogger(HttpRequestClient::class.java)


    val client = OkHttpClient()


    fun request(request: Request): String? {

        val wapper = ObjectWapper<String?>()

        val callback = object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                end()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {

                 response.body()?.string()?.apply {
                     wapper.value = this
                }

                end()
            }

            private fun end() {
                synchronized (wapper) {
                    (wapper as java.lang.Object).notifyAll()
                }
            }

        }
        client.newCall(request).enqueue(callback)

        synchronized (wapper) {
            try {
                (wapper as java.lang.Object).wait()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        }

        logger.info("Http req --> " + request.url() + " \n resp --> " + wapper.value)
        return wapper.value
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
