package com.zzzmode.platfrom.config

import com.zzzmode.platfrom.redis.IRedisPublisher
import com.zzzmode.platfrom.redis.RedisMessageListener
import com.zzzmode.platfrom.redis.RedisPublisherImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CachingConfigurerSupport
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter
import org.springframework.data.redis.serializer.GenericToStringSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * Created by zl on 16/2/28.
 */
@Configuration
open class RedisConfig {

    @Autowired
    private var  template :StringRedisTemplate?=null;


    @Bean
    open fun messageListener() : MessageListenerAdapter {
        return MessageListenerAdapter(RedisMessageListener())
    }


    @Bean
    open fun queueTopic():ChannelTopic{
        return ChannelTopic("pubsub:queue")
    }

    @Bean
    open fun redisContainer():RedisMessageListenerContainer{
        val container= RedisMessageListenerContainer()
        container.connectionFactory=template?.connectionFactory
        container.addMessageListener(messageListener(),queueTopic())
        return container
    }


    @Bean
    open fun redisPublisher(): IRedisPublisher {
        return RedisPublisherImpl()
    }

}