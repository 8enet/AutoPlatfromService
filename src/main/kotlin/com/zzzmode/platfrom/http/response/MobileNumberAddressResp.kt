package com.zzzmode.platfrom.http.response

import com.google.gson.annotations.SerializedName
import com.zzzmode.platfrom.bean.MobileNumberAddress

/**
 * Created by zl on 16/2/16.
 */
class MobileNumberAddressResp {
    var errNum: Int = 0
    var retMsg: String? = null

    @SerializedName("retData")
    var mobileNumberAddress: MobileNumberAddress? = null
}
