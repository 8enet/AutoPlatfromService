package com.zzzmode.platfrom.redis

import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener

/**
 * redis sub/pub 接收
 */
class RedisMessageListener : MessageListener {
    override fun onMessage(message: Message, pattern: ByteArray) {
        println("Message received: " + message.toString())
    }
}