package com.zzzmode.platfrom.services.internal

/**
 * Created by zl on 16/4/16.
 */
interface ICaptchaRecognizeService {

    fun onRecvData(data:ByteArray,port:Int)

    fun onRecognizeSuccess(code:String?,port: Int)

    fun onRecognizeFail(msg:String?,e:Throwable?,port: Int)

}