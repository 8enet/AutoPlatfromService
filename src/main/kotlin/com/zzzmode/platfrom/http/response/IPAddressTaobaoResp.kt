package com.zzzmode.platfrom.http.response

import com.google.gson.annotations.SerializedName
import com.zzzmode.platfrom.bean.IPAddress

/**
 * Created by zl on 16/3/6.
 */
class IPAddressTaobaoResp {
    var code: Int = 0

    @SerializedName("data")
    var ipAddress: IPAddress ?=null
}