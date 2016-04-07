package com.zzzmode.platfrom.dto

/**
 * Created by zl on 16/4/7.
 */
class OrderModel {
    /**
     * 协议version
     */
    var version:Int?=1

    var action:String?=null

    var params:Map<String,String>?=null


    override fun toString(): String{
        return "OrderModel(version=$version, action=$action, params=$params)"
    }


}