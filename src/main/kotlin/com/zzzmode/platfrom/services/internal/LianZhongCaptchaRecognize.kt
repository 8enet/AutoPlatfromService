package com.zzzmode.platfrom.services.internal

import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.SerializedName
import com.zzzmode.platfrom.http.HttpRequestClient
import com.zzzmode.platfrom.util.JsonKit
import com.zzzmode.platfrom.websocket.getCaptchaId
import okhttp3.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.socket.WebSocketSession
import java.io.IOException

/**
 * Created by zl on 16/4/16.
 */
abstract  class LianZhongCaptchaRecognize : AbsCaptchaRecognize() {
    companion object{

        private const val API_SERVER="http://bbb4.hyslt.com/api.php?mod=php&act=upload"
        private const val API_ERROR_REPORT="http://bbb4.hyslt.com/api.php?mod=php&act=error"
        private val logger=LoggerFactory.getLogger(LianZhongCaptchaRecognize::class.java)
    }

    @Value("\${lianzhong_username}")
    var username:String?=null

    @Value("\${lianzhong_pwd}")
    var password:String?=null

    @Value("\${lianzhong_type_mark}")
    var yzmtype_mark:String?="0"

    override  fun onRecvData(data:ByteArray,port:Int){
        if(username.isNullOrEmpty() || password.isNullOrEmpty()){
            logger.warn("not found lianzhong config ! username:${username},password:${password}")

            return
        }

        val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_name",username)
                .addFormDataPart("user_pw",password)
                .addFormDataPart("yzmtype_mark",yzmtype_mark)
                .addFormDataPart("upload", "yzm.jpg",
                        RequestBody.create(MediaType.parse("application/octet-stream"), data))
                .build();

        val callback=object : Callback{
            override fun onFailure(call: Call?, e: IOException?) {
                onRecognizeFail(null,e,port)
            }

            override fun onResponse(call: Call?, response: Response?) {
                response?.let {
                    //success
                    if(response.isSuccessful){
                        var e:Throwable?=null
                        try{
                            JsonKit.gson.fromJson(response.body().string(),ApiResponse::class.java)?.apply {
                                onRecognizeSuccess(this.data?.captchaCode,this.data?.id,port)
                            }
                            e=null
                        }catch(e:Throwable){
                        }

                        if(e is JsonSyntaxException){
                            try {
                                JsonKit.gson.fromJson(response.body().string(),ApiResponseError::class.java)?.apply {
                                    onRecognizeFail(this.data,null,port)
                                }
                                e=null
                            }catch(e:Throwable){
                            }
                        }
                        e?.run {
                            onRecognizeFail(null,e,port)
                        }
                    }
                }
            }
        }


        HttpRequestClient.requestAsync(Request.Builder()
                .url(API_SERVER)
                .post(requestBody)
                .build(),callback)
    }

    override fun onReportError(port: Int) {
        onlineUserManager?.findSessionByPort(port)?.apply {
            onReportError(this)
        }
    }


    override fun onReportError(session: WebSocketSession?) {
        session?.getCaptchaId()?.apply {
            if(!isNullOrEmpty()){
                sendErrorCode(toString())
            }
        }
    }

    private fun sendErrorCode(id: String){
        val callback=object : Callback{
            override fun onFailure(call: Call?, e: IOException?) {
                logger.debug("send error fail: ${e?.message}")
            }

            override fun onResponse(call: Call?, response: Response?) {
                logger.debug("send error resp:${response?.body()?.string()}")
            }
        }
        HttpRequestClient.requestAsync(Request.Builder()
                .url(API_ERROR_REPORT)
                .post(FormBody.Builder()
                        .add("user_name",username)
                        .add("user_pw",password)
                        .add("yzm_id",id)
                        .build())
                .build(),callback)
    }

    data class ApiResponse(var result:Boolean?,@SerializedName("data")var data:ApiResponseVal?)
    data class ApiResponseVal(var id:String?,@SerializedName("val")var captchaCode:String?)

    data class ApiResponseError(var result:Boolean?,var data:String?)
}