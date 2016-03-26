package com.zzzmode.platfrom.redis

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate


/**
 * redis sub/pub 发送
 */
class RedisPublisherImpl() : IRedisPublisher {

    @Autowired
    private var template: StringRedisTemplate? = null;


    override fun publish(channel: String, message: String) {
        template?.convertAndSend(channel, "${message}  --> ${Thread.currentThread().name}")
    }


}