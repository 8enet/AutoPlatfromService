package com.zzzmode.platfrom.services.internal

import org.springframework.web.socket.WebSocketSession

/**
 * Created by zl on 16/4/16.
 */
interface ICaptchaRecognizeService {

    /**
     * 拦截到验证码图片数据
     * @param data 图片byte
     * @param port 代理端口
     */
    fun onRecvData(data:ByteArray,port:Int)

    /**
     * 验证码识别成功
     */
    fun onRecognizeSuccess(code:String?,id:String?,port: Int)

    /**
     * 验证码识别失败
     */
    fun onRecognizeFail(msg:String?,e:Throwable?,port: Int)

    /**
     * 验证码识别错误,报告
     */
    fun onReportError(port: Int)

    /**
     * 验证码识别错误,报告
     */
    fun onReportError(session: WebSocketSession?)

}