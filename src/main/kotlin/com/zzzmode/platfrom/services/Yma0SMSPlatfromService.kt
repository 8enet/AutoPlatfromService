package com.zzzmode.platfrom.services

import com.zzzmode.platfrom.exception.SMSPlatfromException
import com.zzzmode.platfrom.http.HttpRequestClient
import com.zzzmode.platfrom.util.async
import okhttp3.HttpUrl
import okhttp3.Request
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import javax.annotation.PostConstruct

/**
 * Created by zl on 16/2/17.
 */
@Service
open class Yma0SMSPlatfromService {

    companion object {

        private val logger = LoggerFactory.getLogger(Yma0SMSPlatfromService::class.java)

        private val regx_getMobileNum= Pattern.compile("^\\d{11}").toRegex()

        private val regx_getVcodeAndReleaseMobile = Pattern.compile("^\\d{11}.*\\|.*").toRegex()

        class CR{
            companion object{
                const val CN_TELECOM="1"     //中国电信
                const val CN_MOBILE="2"     //中国移动
                const val CN_UNION="3"      //中国联通
            }
        }

        //可以继续轮询的错误
        private  val pollError = arrayListOf("no_data", "unknow_error", "message|please try again later", "not_receive", "sending")

        //错误消息
        private  val errorMsgMap = HashMap<String, String>()

        init {
            errorMsgMap.put("parameter_error", "传入参数错误")
            errorMsgMap.put("not_login", "没有登录,在没有登录下去访问需要登录的资源，忘记传入uid,token")
            errorMsgMap.put("account_is_locked", "账号被锁定")
            errorMsgMap.put("unknow_error", "未知错误,再次请求就会正确返回")
            errorMsgMap.put("message|please try again later", "访问速度过快，建议休眠50毫秒后再试")
            errorMsgMap.put("no_data", "系统暂时没有可用号码了")
            errorMsgMap.put("max_count_disable", "已达到用户可获取号码上限，可通过调用ReleaseMobile方法释放号码并终止任务")
            errorMsgMap.put("mobile_notexists", "指定的号码不存在")
            errorMsgMap.put("mobile_busy", "指定的号码繁忙")
            errorMsgMap.put("not_receive", "还没有接收到验证码,请让程序等待几秒后再次尝试")

        }

    }

    val baseURL: HttpUrl
        @Bean
        get() = HttpUrl.parse(apiServer)

    @Value("\${yma0_api}")
    val apiServer: String? = null


    @Value("\${yma0_uid}")
    val uid: String? = null

    @Value("\${yma0_pwd}")
    val pwd: String? = null

    @Value("\${yma0_pid}")
    val projectId:String?=null

    private var token: String? = null

    @PostConstruct
    fun doLogin(){
        logger.info("uid:${uid},pwd:${pwd},pid:${projectId}")
        async() {
            getToken()
        }
    }


    fun getToken(): String? {
        if (token == null) {
            synchronized (this) {
                if (token == null) {
                    val request = Request.Builder().get().url(getLoginUrl(uid.toString(), pwd.toString())).build()
                    HttpRequestClient.request(request)?.apply {

                        token = this.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                    }
                    logger.info("------------------------")
                    logger.info("getToken token " + token)
                    logger.info("------------------------")
                }
            }
        }
        return token
    }

    /**
     * 获取一个手机号
     */
    fun getMobileNum():String{

        val random= Random()
        val number=StringBuilder("13")
        for(i in 0..8){
            number.append(random.nextInt(9))
        }

        return number.toString()
    }

    /**
     * 获取手机号
     */
    fun getMobileNum2(retry: Int=10):String{
        if(retry == 0){
            logger.error("getMobileNum,too many times to retry")
            throw SMSPlatfromException("getMobileNum fail !")
        }

        fun retry():String{
            sleep(10)
            return getMobileNum2(retry-1)
        }

        val request = Request.Builder().get().url(baseBuilder("getMobilenum")
                .addQueryParameter("pid",projectId)
                .addQueryParameter("cr",CR.CN_MOBILE)
                .build()).build()
        HttpRequestClient.request(request)?.apply {
            regx_getMobileNum.find(this)?.apply {
                return value
            }
            logger.error("getMobileNum error,${this},${errorMsgMap.get(this)}")
            if(holdOnRequest(this)){
                return retry()
            }
        }
        logger.warn("getMobileNum return null ! check network")
        return retry()
    }


    /**
     * 释放已获取的手机号码
     */
    fun releaseMobile(number:String,retry: Int=5){
        if(retry == 0){
            logger.error("releaseMobile fail,number:${number},too many times to retry")
            return
        }

        fun retry(){
            sleep(3)
            releaseMobile(number,retry-1)
        }

        val request = Request.Builder().get().url(baseBuilder("ReleaseMobile").addQueryParameter("mobile",number).build()).build()
        HttpRequestClient.request(request)?.apply {
            if(equals("ok",true)){
                logger.debug("releaseMobile success ! number:${number}")
            }else if(holdOnRequest(this)){
                retry()
                return
            }
        }

        retry()
    }


    /**
     *
     * 获取验证码并不再使用本号
     * @param number 号码
     * @param retry 重试次数
     * @return 接收到的短信内容
     */
    fun getVcodeAndReleaseMobile(number:String,retry:Int=30):String?{
        if(retry == 0){
            logger.error("getVcodeAndReleaseMobile fail,number:${number},too many times to retry")
            addIgnoreList(number)
            return null
        }
        //java.lang.VerifyError: Bad type on operand stack
        //这里不能使用嵌套?上面的都可以,不知道为什么.

//        fun retry(): String? {
//            sleep(5)
//            return getVcodeAndReleaseMobile(number,retry-1)
//        }

        val request = Request.Builder().get().url(baseBuilder("getVcodeAndReleaseMobile").addQueryParameter("mobile",number).build()).build()
        HttpRequestClient.request(request)?.apply {
            //成功返回：手机号码|验证码短信
            if(regx_getVcodeAndReleaseMobile.matches(this)){
                return split("|")[1].trim()
            }
            logger.error("getVcodeAndReleaseMobile error,number:${number},retry:${retry},${this},${errorMsgMap.get(this)}")

            if(holdOnRequest(this)){
                sleep(5)
                return getVcodeAndReleaseMobile(number,retry-1)
                //return retry()
            }
        }
        logger.error("getVcodeAndReleaseMobile response is null ! number:${number}")
        sleep(5)
        return getVcodeAndReleaseMobile(number,retry-1)
        //return retry()
    }


    /**
     * 加黑无用号码
     * @param number 号码
     * @param retry 重试次数
     */
    fun addIgnoreList(number:String,retry: Int=5){
        if(retry == 0){
            logger.error("addIgnoreList,number:${number},too many times to retry")
            return
        }

        fun retry(){
            sleep(5)
            addIgnoreList(number,retry-1)
        }

        val request = Request.Builder().get().url(baseBuilder("addIgnoreList").addQueryParameter("mobiles",number).build()).build()
        HttpRequestClient.request(request)?.apply {
            if(holdOnRequest(this)){
                logger.warn("addIgnoreList error! will retry,number:${number}")
                retry()
                return
            }
        }

        retry()
    }

    /**
     * 发送短信
     * @param number 获取到的手机号
     * @param recv 接收号码
     * @param content 短信内容
     */
    fun sendSms(number: String,recv:String,content:String,retry: Int=3){
        if(retry == 0){
            logger.error("sendSms fail,number:${number},recv:${recv},content:${content},too many times to retry")
            return
        }
        val request = Request.Builder().get().url(baseBuilder("sendSms")
                .addQueryParameter("mobile",number)
                .addQueryParameter("recv",recv)
                .addQueryParameter("content",content)
                .build())
                .build()
        HttpRequestClient.request(request)?.apply {
            if(equals("ok",true)){
                logger.debug("sendSms success! number:${number},recv:${recv},content:${content}")
            }else if(holdOnRequest(this)){
                sleep(5)
                logger.warn("sendSms error! will retry ,number:${number},recv:${recv},content:${content}")
                sendSms(number,recv,content,retry-1)
            }
        }
    }

    /**
     * 获取短信发送状态
     * 该方法使用于发送短信后，检查短信是否发送成功
     * @param number 获取到的手机号
     */
    fun getSmsStatus(number: String,retry: Int=10):Boolean?{
        if(retry == 0){
            logger.error("getSmsStatus fail,number:${number},too many times to retry")
            return false
        }

        fun retry():Boolean?{
            sleep(5)
            return  getSmsStatus(number,retry-1)
        }

        val request = Request.Builder().get().url(baseBuilder("getSmsStatus")
                .addQueryParameter("mobile",number)
                .build())
                .build()
        HttpRequestClient.request(request)?.apply {
            if(equals("succ",true)){
                logger.debug("getSmsStatus success! number:${number}")
                return true
            }else if(holdOnRequest(this)){
                logger.warn("getSmsStatus error! will retry ,number:${number}")
                return  retry()
            }
        }
        return retry()
    }

    val userInfo: String?
        get() {
            val request = Request.Builder().get().url(baseBuilder("getUserInfos").build()).build()
            return HttpRequestClient.request(request)
        }

    //是否需要继续请求
    private fun holdOnRequest(err:String):Boolean{
        return pollError.contains(err)
    }

    private fun sleep(timeout:Long=1){
        TimeUnit.SECONDS.sleep(timeout)
    }

    private fun getLoginUrl(uid: String, pwd: String): HttpUrl {
        return baseURL.newBuilder()
                .addQueryParameter("action", "loginIn")
                .addQueryParameter("uid", uid)
                .addQueryParameter("pwd", pwd)
                .build()
    }

    private fun baseBuilder(action:String):HttpUrl.Builder{
        return  baseURL.newBuilder()
                .addQueryParameter("action", action)
                .addQueryParameter("uid", uid)
                .addQueryParameter("pid",projectId)
                .addQueryParameter("author_uid",uid)
                .addQueryParameter("token", token)
    }

}
