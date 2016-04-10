package com.zzzmode.platfrom.dto

import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 一个虚拟用户
 * Created by zl on 16/2/28.
 */
@Document
class VirtualUser :AbstractDocument(){


    /**
     * 获取到的手机号
     */
    @Indexed(unique = true)
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

    /**
     * 创建时间
     */
    var createdDate:Long?=System.currentTimeMillis()



    override fun toString(): String{
        return "VirtualUser(id=$id,phone=$phone, province=$province, city=$city, proxyPort=$proxyPort, address=$address, userName=$userName, userPwd=$userPwd, email=$email, createdDate=$createdDate)"
    }


}