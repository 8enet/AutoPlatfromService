package com.zzzmode.platfrom.http.response

import com.google.gson.annotations.SerializedName

/**
 * Created by zl on 16/3/6.
 */
class BaiduPlaceResp {

    var status:Int=0

    var total:Int=0

    @SerializedName("results")
    var addressDatas:List<ResultAddress>?=null


    class ResultAddress{
        var name:String?=null
        var address:String?=null
    }
}