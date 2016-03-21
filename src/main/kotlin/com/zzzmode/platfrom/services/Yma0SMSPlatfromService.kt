package com.zzzmode.platfrom.services

import com.zzzmode.platfrom.http.HttpRequestClient
import okhttp3.HttpUrl
import okhttp3.Request
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowire
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseBody
import java.util.*

import java.util.concurrent.TimeUnit

/**
 * Created by zl on 16/2/17.
 */
@Service
open class Yma0SMSPlatfromService {

    val baseURL: HttpUrl
        @Bean
        get() = HttpUrl.parse(apiServer)

    @Value("\${yma0_api}")
    val apiServer: String? = null


    @Value("\${yma0_uid}")
    val uid: String? = null

    @Value("\${yma0_pwd}")
    val pwd: String? = null

    private var token: String? = null


    fun getToken(): String? {
        if (token == null) {
            synchronized (this) {
                if (token == null) {
                    val request = Request.Builder().get().url(getLoginUrl(uid.toString(), pwd.toString())).build()
                    HttpRequestClient.request(request)?.apply {

                        token = this.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                    }

                    logger.info("getToken token " + token)
                }
            }
        }
        return token
    }

    fun getMobileNum():String{

        TimeUnit.SECONDS.sleep(5)

        val random= Random()
        val number=StringBuilder("13")
        for(i in 0..8){
            number.append(random.nextInt(9))
        }

        return number.toString()
    }


    val userInfo: String?
        get() {
            getToken()?.apply {
                val request = Request.Builder().get().url(getUserInfoUrl(uid.toString(), this)).build()
                return HttpRequestClient.request(request)
            }
            return null
        }


    private fun getLoginUrl(uid: String, pwd: String): HttpUrl {
        return baseURL.newBuilder().addQueryParameter("action", "loginIn").addQueryParameter("uid", uid).addQueryParameter("pwd", pwd).build()
    }

    private fun getUserInfoUrl(uid: String, token: String): HttpUrl {
        return baseURL.newBuilder().addQueryParameter("action", "getUserInfos").addQueryParameter("uid", uid).addQueryParameter("token", token).build()
    }

    companion object {

        private val logger = LoggerFactory.getLogger(Yma0SMSPlatfromService::class.java)
    }


}
