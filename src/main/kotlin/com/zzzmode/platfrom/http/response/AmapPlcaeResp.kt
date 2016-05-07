package com.zzzmode.platfrom.http.response

import com.google.gson.annotations.SerializedName

/**
 * Created by zl on 16/5/7.
 */
class AmapPlcaeResp {

    var status:String="1"

    var infocode:String="10000"

    var count:String="0"

    @SerializedName("pois")
    var addressDatas:List<ResultAddress>?=null


    class ResultAddress{
        var pname:String?=null
        var cityname:String?=null
        var adname:String?=null

        var name:String?=null
        var address:String?=null
    }
}