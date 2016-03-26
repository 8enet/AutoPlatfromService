package com.zzzmode.platfrom.services

import com.zzzmode.platfrom.dto.VirtualUser
import com.zzzmode.platfrom.util.Utils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.net.InetSocketAddress
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.concurrent.thread

/**
 * Created by zl on 16/3/6.
 */
@Service
open class UserService {

    @Autowired
    var smsPlatfromService : Yma0SMSPlatfromService?=null

    @Autowired
    var toolsService : ToolsService?=null

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
        }else{
            user=preUsers.poll()
        }

        onlineUsers.put(user.id,user)

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

    /**
     * 获取用户信息
     */
    fun getUser(id: Int):VirtualUser?{
        return onlineUsers.get(id)
    }

    private fun loadUser(){

        preUsers.add(newUser())

    }

    private fun newUser():VirtualUser{
        val user=VirtualUser(Utils.generateUserId())
        //1.获取手机号
        user.phone=smsPlatfromService?.getMobileNum()
        //2.根据手机号获取区域ip
        toolsService?.getMobileAddress(user.phone!!)?.apply {
            //地区
            user.province=this.province
            user.city=this.city

            //详细地址
            user.address=toolsService?.getAddress(user.province!!,user.city!!)

            //代理端口
            user.proxyPort=8099
        }

        //用户名密码
        val (username, pwd) = toolsService!!.getUsernameAndPwd()
        user.userName=username
        user.userPwd=pwd

        return user
    }

}