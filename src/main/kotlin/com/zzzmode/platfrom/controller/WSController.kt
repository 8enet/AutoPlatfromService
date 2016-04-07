package com.zzzmode.platfrom.controller

import com.zzzmode.platfrom.dto.OrderModel
import com.zzzmode.platfrom.services.ToolsService
import com.zzzmode.platfrom.services.UserService
import com.zzzmode.platfrom.util.JsonKit
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

/**
 * Created by zl on 16/4/7.
 */
@Component
@ComponentScan
class WSController {

    companion object{
        private val logger=LoggerFactory.getLogger(WSController::class.java)
    }

    @Autowired
    var userService: UserService?=null

    @Autowired
    var toolsService: ToolsService?=null

    fun onRecvAction(orderModel: OrderModel?,session: WebSocketSession?){
        orderModel?.action?.apply {
            when(this){
                "query" -> query(orderModel,session)
                "user"  -> user(orderModel,session)
            }
        }
    }

    private fun query(orderModel: OrderModel?,session: WebSocketSession?){
        orderModel?.params?.get("ip")?.apply {
            toolsService?.getIpInfo(this)?.apply {
                session?.sendMessage(TextMessage(JsonKit.gson.toJson(this)))
            }
            return@query
        }

        orderModel?.params?.get("phone")?.apply {
            toolsService?.getMobileAddress(this)?.apply {
                session?.sendMessage(TextMessage(JsonKit.gson.toJson(this)))
            }
            return@query
        }
    }

    private fun user(orderModel: OrderModel?,session: WebSocketSession?){
        logger.info(orderModel?.toString())
        //find user by id
        orderModel?.params?.get("id")?.apply {
            userService?.getUser(Integer.parseInt(this))?.apply {
                session?.sendMessage(TextMessage(JsonKit.gson.toJson(this)))
            }
            return@user
        }

        //create user
        orderModel?.apply {

            userService?.obainUser()?.apply {
                session?.sendMessage(TextMessage(JsonKit.gson.toJson(this)))
            }
            return@user
        }

    }

}