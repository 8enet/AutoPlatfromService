package com.zzzmode.platfrom.controller

import com.zzzmode.platfrom.dto.OrderModel
import com.zzzmode.platfrom.dto.ResponseModel
import com.zzzmode.platfrom.dto.VirtualUser
import com.zzzmode.platfrom.services.OnlineUserManager
import com.zzzmode.platfrom.services.ToolsService
import com.zzzmode.platfrom.services.UserService
import com.zzzmode.platfrom.util.JsonKit
import com.zzzmode.platfrom.util.isNotNull
import com.zzzmode.platfrom.util.isNull
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
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


    @Autowired
    var onlineUserManager: OnlineUserManager?=null


    fun onRecvAction(orderModel: OrderModel?,session: WebSocketSession?){
        orderModel?.module?.apply {

            when(this){
                OrderModel.Module.TOOLS -> toolsQuery(orderModel,session)
                OrderModel.Module.USER  -> user(orderModel,session)
            }
        }
    }

    private fun toolsQuery(orderModel: OrderModel,session: WebSocketSession?){
        orderModel.params?.get("ip")?.apply {
            toolsService?.getIpInfo(this)?.apply {
                session?.sendMessage(TextMessage(JsonKit.gson.toJson(this)))
            }
            return@toolsQuery
        }

        orderModel.params?.get("phone")?.apply {
            toolsService?.getMobileAddress(this)?.apply {
                session?.sendMessage(TextMessage(JsonKit.gson.toJson(this)))
            }
            return@toolsQuery
        }
    }

    private fun user(orderModel: OrderModel,session: WebSocketSession?){
        var resp:ResponseModel<VirtualUser>?=null

        when(orderModel.action){
            OrderModel.Action.GET ->
                resp=user_get(orderModel)
            OrderModel.Action.ADD ->
                resp=user_add()
            OrderModel.Action.DELETE ->
                resp=user_delete(orderModel)
            OrderModel.Action.UPDATE ->
                print("")
            else -> resp=user_get(orderModel)
        }
        if(resp == null){
            resp= ResponseModel<VirtualUser>()
            resp.status=false
            resp.data=null
            resp.msg="error request -> $orderModel"
        }else{
            //session add user
            if(OrderModel.Action.ADD.equals(orderModel.action)){
                session?.attributes?.put("user",resp.data)
                session?.attributes?.put("proxy_port",resp.data?.proxyPort)

                if(session.isNotNull() && resp.data.isNotNull()){
                    onlineUserManager?.addUser(session!!,resp.data!!)
                }

            }
        }
        logger.info("send msg -> "+resp)
        session?.sendMessage(TextMessage(JsonKit.gson.toJson(resp)))

    }


    private fun user_get(orderModel: OrderModel):ResponseModel<VirtualUser>{
        val resp=ResponseModel<VirtualUser>()
        orderModel.params?.get("id")?.apply {
            if(!StringUtils.isEmpty(this)) {
                userService?.getUser(toLong())?.run {
                    resp.status = true
                    resp.msg = null
                    resp.data = this

                    return resp
                }
            }
        }

        resp.status=false
        resp.msg="not found user ,params: ${orderModel.params}"
        resp.data=null
        return resp
    }

    private fun user_add():ResponseModel<VirtualUser>{
        val resp=ResponseModel<VirtualUser>()
        userService?.obainUser()?.run {
            resp.status=true
            resp.msg=null
            resp.data=this
            return resp
        }

        resp.status=false
        resp.data=null
        resp.msg="create user fail!"
        return resp
    }

    private fun user_delete(orderModel: OrderModel):ResponseModel<VirtualUser>{
        val resp=ResponseModel<VirtualUser>()
        orderModel.params?.get("id")?.run {
            if(!StringUtils.isEmpty(this)) {
                userService?.deleteUser(toLong())?.run {
                    resp.status = this
                    resp.msg = null
                    resp.data = null

                    return resp
                }
            }
        }
        resp.status=false
        resp.data=null
        resp.msg="delete user fail,params ${orderModel.params}"
        return resp
    }

}