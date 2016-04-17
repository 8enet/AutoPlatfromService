package com.zzzmode.platfrom.services.manager

import com.zzzmode.platfrom.dto.VirtualUser
import com.zzzmode.platfrom.services.HttpProxyService
import com.zzzmode.platfrom.websocket.getUserProxyPort
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by zl on 16/4/16.
 */
class OnlineUserManager {

    private val onlineUsers = ConcurrentHashMap<WebSocketSession, VirtualUser>()

    @Autowired
    var httpProxyService: HttpProxyService? = null

    fun addUser(session: WebSocketSession, user: VirtualUser){
        onlineUsers.put(session,user)
    }

    fun removeUser(session: WebSocketSession){
        onlineUsers.remove(session)
        session.getUserProxyPort()?.apply {
            httpProxyService?.stopProxyServer(this)
        }
    }

    fun findSessionByPort(port:Int): WebSocketSession?{

        onlineUsers.forEach {
            it.key.getUserProxyPort()?.apply {
                if(this.equals(port)){
                    return it.key
                }
            }
        }
        return null
    }

    fun findUserByPort(port: Int): VirtualUser?{
        onlineUsers.forEach {
            it.key.getUserProxyPort()?.apply {
                if(this is Int && this.equals(port)){
                    return it.value
                }
            }
        }
        return null
    }

}