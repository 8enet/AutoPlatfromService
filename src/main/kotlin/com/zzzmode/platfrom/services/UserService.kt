package com.zzzmode.platfrom.services

import com.zzzmode.platfrom.dto.VirtualUser
import com.zzzmode.platfrom.util.Utils
import com.zzzmode.platfrom.util.generateUserId
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.concurrent.thread

/**
 * Created by zl on 16/3/6.
 */
@Service
open class UserService {

    //在线用户
    private val onlineUsers= ConcurrentHashMap<Int, VirtualUser>()

    //预生成用户
    private val preUsers= ConcurrentLinkedQueue<VirtualUser>()

    /**
     * 获取一个新用户
     */
    open fun obainUser(): VirtualUser {
        val user:VirtualUser
        if(preUsers.isEmpty()){
            user=newUser()
            onlineUsers.put(user.id,user)
        }else{
            user=preUsers.poll()
        }

        //异步生成一个用户
        thread {
            loadUser()
        }

        return user
    }

    /**
     * 释放用户资源
     */
    open fun relsaseUser(id:Int){
        onlineUsers.get(id)?.apply {
            onlineUsers.remove(id)

            println(this)
        }
    }

    /**
     * 检测是否在线
     */
    open fun online(id:Int):Boolean{
        return onlineUsers.containsKey(id)
    }


    private fun loadUser(){

        preUsers.add(newUser())

    }

    private fun newUser():VirtualUser{
        val user=VirtualUser(Utils.generateUserId())

        return user
    }

}