package com.zzzmode.platfrom.redis

import org.springframework.beans.factory.annotation.Autowired
import java.util.concurrent.atomic.AtomicLong

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.scheduling.annotation.Scheduled


class RedisPublisherImpl() : IRedisPublisher {

    @Autowired
    private var template: StringRedisTemplate? = null;


    override fun publish(channel: String, message: String) {
        template?.convertAndSend(channel, "${message}  --> ${Thread.currentThread().name}")
    }


}