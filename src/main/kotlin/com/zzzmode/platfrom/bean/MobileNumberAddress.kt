package com.zzzmode.platfrom.bean

/**
 * Created by zl on 16/2/16.
 */
 class MobileNumberAddress (
        var phone: String? = null,
        var supplier: String? = null,
        var province: String? = null,
        var city: String? = null,
        var suit: String? = null
){




    override fun toString(): String {
        return "MobileNumberAddress{phone='$phone\', supplier='$supplier\', province='$province\', city='$city\', suit='$suit\'}"
    }
}
