package com.zzzmode.platfrom.services

import com.zzzmode.platfrom.dao.repository.UserRepository
import com.zzzmode.platfrom.dto.VirtualUser
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service

/**
 * Created by zl on 16/3/6.
 */
@Service
open class UserService {

    companion object {
        private val logger = LoggerFactory.getLogger(UserService::class.java)
    }

    @Autowired
    var smsPlatfromService: Yma0SMSPlatfromService? = null

    @Autowired
    var toolsService: ToolsService? = null

    @Autowired
    var httpProxyService: HttpProxyService? = null

    @Autowired
    var userRepository: UserRepository? = null


    @Bean
    @ConditionalOnMissingBean
    fun onlineUserManagerCreate(): OnlineUserManager {
        return OnlineUserManager()
    }

    /**
     * 获取一个新用户
     */
    fun obainUser(): VirtualUser {
        return newUser()
    }

    /**
     * 释放用户资源
     */
    fun relsaseUser(id: Long) {
        getUser(id)?.apply {
            httpProxyService?.stopProxyServer(this.proxyPort!!)
        }
    }

    fun deleteUser(id: Long): Boolean {
        userRepository?.delete(id)?.run {
            return this
        }
        return false
    }


    /**
     * 获取用户信息
     */
    fun getUser(id: Long): VirtualUser? {

        return userRepository?.findOne(id)
    }

    fun getUsers(): List<VirtualUser>? {
        return userRepository?.findAll()
    }


    private fun newUser(): VirtualUser {
        if (httpProxyService == null) {
            throw RuntimeException(" httpProxyService is null !!!")
        }

        val user = VirtualUser()
        //1.获取手机号
        user.phone = smsPlatfromService?.getMobileNum()
        //2.根据手机号获取区域ip
        toolsService?.getMobileAddress(user.phone!!)?.apply {
            //地区
            user.province = this.province
            user.city = this.city

            //详细地址
            user.address = toolsService?.getAddress(user.province, user.city)

            //代理端口
            user.proxyPort = httpProxyService?.getNextPort()

            //启动代理
            httpProxyService?.startProxyServer(user.proxyPort!!, toolsService?.getProxy(user))
        }

        //用户名密码
        val (username, pwd) = toolsService!!.getUsernameAndPwd()
        user.userName = username
        user.userPwd = pwd

        logger.info("newUser -->> " + user)
        try {
            userRepository?.save(user)
        } catch(e: Exception) {
            e.printStackTrace()
            logger.error("mongodb save user error !!!", e)
        }
        return user
    }

}