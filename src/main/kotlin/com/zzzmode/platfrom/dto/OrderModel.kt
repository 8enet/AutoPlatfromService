package com.zzzmode.platfrom.dto

/**
 * Created by zl on 16/4/7.
 */
class OrderModel {

    class Module{
        private  constructor()

        companion object{
            val TOOLS="tools"     //工具
            val USER="user"       //用户
            val CAPTCHA="captcha" //验证码模块
        }

    }

    class Action{
        private  constructor()
        companion object{
            val ADD="add"
            val DELETE="delete"
            val UPDATE="update"
            val GET="get"
        }
    }



    /**
     * 协议version
     */
    var version:Int?=1

    var module:String?=null

    var action:String?=null

    var params:Map<String,String>?=null


    override fun toString(): String{
        return "OrderModel(version=$version, action=$action, params=$params)"
    }


}