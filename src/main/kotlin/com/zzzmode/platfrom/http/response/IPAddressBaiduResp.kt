package com.zzzmode.platfrom.http.response

import com.google.gson.annotations.SerializedName
import com.zzzmode.platfrom.bean.IPAddress

/**
 * Created by zl on 16/3/6.
 */
class IPAddressBaiduResp {
    var errNum: Int = 0
    var errMsg:String?=null

    @SerializedName("retData")
    var ipAddress: IPAddress ?=null
}