package com.zzzmode.platfrom.services

import com.zzzmode.platfrom.dto.ResponseModel
import com.zzzmode.platfrom.services.internal.LianZhongCaptchaRecognize
import com.zzzmode.platfrom.util.JsonKit
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage

/**
 * Created by zl on 16/4/16.
 */
@Service
class CaptchaRecognizeService : LianZhongCaptchaRecognize(){

    companion object{
        private val logger=LoggerFactory.getLogger(CaptchaRecognizeService::class.java)
    }

    @Autowired
    var onlineUserManager: OnlineUserManager?=null


    override fun onRecognizeSuccess(code: String?, port: Int) {
        code?.run {
            notify(port,ResponseModel<CaptchaResponse>(true,null,CaptchaResponse(code)))
        }
    }

    override fun onRecognizeFail(msg: String?, e: Throwable?, port: Int) {
        msg?.run {
            notify(port,ResponseModel<CaptchaResponse>(false,msg))
        }
    }

    private fun notify(port: Int,resp:ResponseModel<CaptchaResponse>){
        logger.debug("notify port:${port},resp:${resp}")
        onlineUserManager?.findSessionByPort(port)?.apply {
            sendMessage(TextMessage(JsonKit.gson.toJson(resp)))
        }
    }

    data class CaptchaResponse(var code: String?,var msg: String?=null)

}