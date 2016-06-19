package com.zzzmode.platfrom.dto

import com.google.gson.annotations.SerializedName

/**
 * 短信消息, 区分发送/接收方
 * Created by zl on 16/6/19.
 */
class SMSInfo {
    constructor()

    constructor(recvPhone:String?,sendPhone:String?,content:String?){
        this.recvPhone=recvPhone
        this.sendPhone=sendPhone
        this.content=content
    }

    /**
     * 接收方号码
     */
    @SerializedName("recv_phone")
    var recvPhone:String?=null

    /**
     * 发送方号码
     */
    @SerializedName("send_phone")
    var sendPhone:String?=null

    /**
     * 发送/接收  内容
     */
    @SerializedName("content")
    var content:String?=null


}