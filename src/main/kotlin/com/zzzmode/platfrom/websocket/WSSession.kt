package com.zzzmode.platfrom.websocket

import com.zzzmode.platfrom.dto.VirtualUser
import org.springframework.web.socket.WebSocketSession

/**
 * Created by zl on 16/4/17.
 */

fun WebSocketSession?.getUserProxyPort():Int?{
    this?.attributes?.get("proxy_port")?.apply {
        if(this is Int){
            return this
        }
    }
    return null
}

fun WebSocketSession?.setUserProxyPort(port: Int?){
    port?.let {
        this?.attributes?.put("proxy_port",port)
    }

}

fun WebSocketSession?.getUser():VirtualUser?{
    this?.attributes?.get("user")?.apply {
        if(this is VirtualUser){
            return this
        }
    }
    return null
}

fun WebSocketSession?.setUser(user:VirtualUser?){
    user?.let {
        this?.attributes?.put("user",user)
    }
}

fun WebSocketSession?.setCaptchaId(id:String?){
    id?.let {
        this?.attributes?.put("code_id",id)
    }
}

fun WebSocketSession?.getCaptchaId():String?{
    this?.attributes?.get("code_id")?.apply {
        if(this is String){
            return this
        }
    }
    return null
}
