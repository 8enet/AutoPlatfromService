package com.zzzmode.platfrom.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**
 * Created by zl on 16/2/16.
 */
class  JsonKit {

    companion object{
        val gson =  GsonBuilder().serializeNulls().create();
    }
}
