package com.zzzmode.platfrom.dto

import java.net.InetSocketAddress

/**
 * 一个虚拟用户
 * Created by zl on 16/2/28.
 */
class VirtualUser (var id: Int){



    /**
     * 获取到的手机号
     */
    var phone: String ? = null

    /**
     * 所在省份
     */
    var province: String ? = null

    /**
     * 城市
     */
    var city: String ? = null

//    /**
//     * 获取到的代理ip
//     */
//    var proxyHost: String ? = null

    /**
     * 获取代理端口
     */
    var proxyPort:Int?=8099

    /**
     * 详细地址
     */
    var address:String?=null


    /**
     * 用户名
     */
    var userName:String?=null

    /**
     * 密码
     */
    var userPwd:String?=null

    /**
     * 邮箱
     */
    var email:String?=null


}