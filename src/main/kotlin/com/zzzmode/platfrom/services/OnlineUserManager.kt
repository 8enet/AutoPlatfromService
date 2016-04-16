package com.zzzmode.platfrom.services

import com.zzzmode.platfrom.dto.VirtualUser
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by zl on 16/4/16.
 */
class OnlineUserManager {

    private val onlineUsers = ConcurrentHashMap<WebSocketSession, VirtualUser>()

    fun addUser(session: WebSocketSession,user:VirtualUser){
        onlineUsers.put(session,user)
    }

    fun removeUser(session: WebSocketSession){
        onlineUsers.remove(session)
    }

    fun findSessionByPort(port:Int):WebSocketSession?{

        onlineUsers.forEach {
            it.key.attributes?.get("proxy_port")?.apply {
                if(this is Int && this.equals(port)){
                    return it.key
                }
            }
        }
        return null
    }

    fun findUserByPort(port: Int):VirtualUser?{
        onlineUsers.forEach {
            it.key.attributes?.get("proxy_port")?.apply {
                if(this is Int && this.equals(port)){
                    return it.value
                }
            }
        }
        return null
    }

}