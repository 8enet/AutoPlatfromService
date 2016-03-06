package com.zzzmode.platfrom.bean

import com.google.gson.annotations.SerializedName

/**
 * Created by zl on 16/3/6.
 */
class IPAddress {

    var ip: String? = null // ip地址

    var country:String? =null //国家

    @SerializedName("region",alternate=arrayOf("province"))
    var region: String? = null //省

    var city: String? = null   //市
    var district: String? = null //区

    @SerializedName("isp",alternate = arrayOf("carrier"))
    var isp: String? = null      //运营商

    override fun toString(): String{
        return "IPAddress(ip=$ip, region=$region, city=$city, district=$district, isp=$isp)"
    }
}