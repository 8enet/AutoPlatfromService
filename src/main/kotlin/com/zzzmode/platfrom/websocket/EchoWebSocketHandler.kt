package com.zzzmode.platfrom.websocket

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler


open class EchoWebSocketHandler
@Autowired
constructor(private val echoService: EchoService) : TextWebSocketHandler() {

    override fun afterConnectionEstablished(session: WebSocketSession?) {
        logger.info("Opened new session in instance " + session!!.remoteAddress + "   " + session.id)
    }

    @Throws(Exception::class)
    public override fun handleTextMessage(session: WebSocketSession?, message: TextMessage?) {
        val echoMessage = this.echoService.getMessage(message!!.payload)
        logger.info(echoMessage)
        session!!.sendMessage(TextMessage("server recv --> " + echoMessage))
    }

    @Throws(Exception::class)
    override fun handleTransportError(session: WebSocketSession?, exception: Throwable?) {
        session!!.close(CloseStatus.SERVER_ERROR)
    }

    @Throws(Exception::class)
    override fun afterConnectionClosed(session: WebSocketSession?, status: CloseStatus?) {
        super.afterConnectionClosed(session, status)

        logger.info("session " + session!!.remoteAddress + "   " + session.id + "   closed ")
    }

    companion object {

        private val logger = LoggerFactory.getLogger(EchoWebSocketHandler::class.java)
    }
}