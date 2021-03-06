package com.zzzmode.platfrom.config

import com.zzzmode.platfrom.websocket.WSSocketHandler
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Controller
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.adapter.standard.WebSocketToStandardExtensionAdapter
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

/**
 * Created by zl on 16/2/17.
 */
@Configuration
@EnableAutoConfiguration
@EnableWebSocket
open class WebSocketConfig : WebSocketConfigurer {


    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(echoWebSocketHandler(), "/control").setAllowedOrigins("*")
    }

    @Bean
    open fun echoWebSocketHandler(): WebSocketHandler {
        return WSSocketHandler()
    }

}
