package com.zzzmode.platfrom.bean

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.google.gson.annotations.SerializedName

/**
 * Created by zl on 16/2/16.
 */
@JacksonXmlRootElement(localName = "root")
 class MobileNumberAddress{


    @SerializedName("phone",alternate = arrayOf("mobile"))
    @JacksonXmlProperty(localName = "chgmobile")
    var phone: String? = null

    @SerializedName("supplier",alternate = arrayOf("isp"))
    @JacksonXmlProperty(localName = "supplier")
    var supplier: String? = null

   @JacksonXmlProperty(localName = "province")
    var province: String? = null

    @SerializedName("city",alternate = arrayOf("cityname"))
    @JacksonXmlProperty(localName = "city")
    var city: String? = null


    override fun toString(): String {
        return "MobileNumberAddress{phone='$phone\', supplier='$supplier\', province='$province\', city='$city\'}"
    }
}
