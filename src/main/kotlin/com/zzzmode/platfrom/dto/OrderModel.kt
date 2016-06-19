package com.zzzmode.platfrom.dto

import java.util.*

/**
 * Created by zl on 16/4/7.
 */
class OrderModel {

    class Module{
        private  constructor()

        companion object{
            const val TOOLS="tools"     //工具
            const val USER="user"       //用户
            const val CAPTCHA="captcha" //验证码模块
            const val SMS="sms"         //短信模块
        }

    }

    class Action{
        private  constructor()
        companion object{
            const val ADD="add"
            const val DELETE="delete"
            const val UPDATE="update"
            const val GET="get"
            const val SEND="send"
        }
    }



    /**
     * 协议version
     */
    var version:Int?=1

    var module:String?=null

    var action:String?=null

    var params=HashMap<String,String>(2)

    fun clear(){
        module=null
        action=null
        params.clear()
    }

    fun addParam(key:String,value:String){

        params.put(key,value)
    }

    override fun toString(): String{
        return "OrderModel(version=$version, action=$action, params=$params)"
    }


}