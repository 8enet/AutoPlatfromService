package com.zzzmode.platfrom.redis

/**
 * Created by zl on 16/2/28.
 */
interface IRedisPublisher {
    fun publish(channel:String,message:String)
}