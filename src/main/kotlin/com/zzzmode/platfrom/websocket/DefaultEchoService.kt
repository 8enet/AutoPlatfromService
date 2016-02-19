package com.zzzmode.platfrom.websocket

class DefaultEchoService : EchoService {

    override fun getMessage(message: String): String {
        return message
    }

}