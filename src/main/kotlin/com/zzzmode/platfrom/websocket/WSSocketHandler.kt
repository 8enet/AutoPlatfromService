package com.zzzmode.platfrom.websocket

import com.zzzmode.platfrom.controller.WSController
import com.zzzmode.platfrom.dto.OrderModel
import com.zzzmode.platfrom.services.manager.OnlineUserManager
import com.zzzmode.platfrom.util.JsonKit
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@ComponentScan
open class WSSocketHandler : TextWebSocketHandler() {

    companion object {
        private val logger = LoggerFactory.getLogger(WSSocketHandler::class.java)
    }

    @Autowired
    val wsController: WSController?=null

    @Autowired
    var onlineUserManager: OnlineUserManager?=null

    override fun afterConnectionEstablished(session: WebSocketSession?) {

        logger.info("Opened new session in instance " + session?.remoteAddress + "   " + session?.id+"  "+session?.acceptedProtocol)
    }

    @Throws(Exception::class)
    public override fun handleTextMessage(session: WebSocketSession?, message: TextMessage?) {
        val echoMessage = message!!.payload
        logger.info("server recv --> " + echoMessage)
        try{
            wsController?.onRecvAction(JsonKit.gson.fromJson(echoMessage,OrderModel::class.java),session)
        }catch(e:Throwable){
        }

    }

    @Throws(Exception::class)
    override fun handleTransportError(session: WebSocketSession?, exception: Throwable?) {
        session?.close(CloseStatus.SERVER_ERROR)
    }

    @Throws(Exception::class)
    override fun afterConnectionClosed(session: WebSocketSession?, status: CloseStatus?) {
        super.afterConnectionClosed(session, status)

        logger.info("session " + session?.remoteAddress + "   " + session?.id + "   closed ")

        session?.run {
            onlineUserManager?.removeUser(session)
        }

    }


}