package com.zzzmode.platfrom.bean

import com.google.gson.annotations.SerializedName

/**
 * Created by zl on 16/2/16.
 */
 class MobileNumberAddress{


    @SerializedName("phone",alternate = arrayOf("mobile"))
    var phone: String? = null

    @SerializedName("supplier",alternate = arrayOf("isp"))
    var supplier: String? = null

    var province: String? = null

    @SerializedName("city",alternate = arrayOf("cityname"))
    var city: String? = null


    override fun toString(): String {
        return "MobileNumberAddress{phone='$phone\', supplier='$supplier\', province='$province\', city='$city\'}"
    }
}
