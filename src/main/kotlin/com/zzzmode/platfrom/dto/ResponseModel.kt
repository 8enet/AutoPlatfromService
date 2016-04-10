package com.zzzmode.platfrom.dto

import java.io.Serializable

/**
 * Created by zl on 16/4/10.
 */

class ResponseModel<T> constructor(var status:Boolean?=false,var msg:String?=null,var data:T?=null) : Serializable{

    override fun toString(): String{
        return "ResponseModel(status=$status, msg=$msg, data=$data)"
    }


}