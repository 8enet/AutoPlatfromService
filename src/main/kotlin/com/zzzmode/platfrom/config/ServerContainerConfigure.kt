package com.zzzmode.platfrom.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * 服务器环境配置
 * Created by zl on 16/5/7.
 */
@Component
@ConfigurationProperties(prefix = "server", ignoreUnknownFields = true)
open class ServerContainerConfigure {

    var http: Http= Http()

    var http2: Http2= Http2()

    class Http{
        /**
         * Http端口
         */
        var port:Int=8080


        /**
         * 是否启用http
         */
        var enabled:Boolean=false

        /**
         * 是否强制http重定向到https
         */
        var redirect2Https:Boolean=false

        override fun toString(): String{
            return "Http(port=$port, enabled=$enabled, redirect2Https=$redirect2Https)"
        }
    }

    class Http2{
        /**
         * 是否启用http/2.0支持
         */
        var enabled:Boolean=false


        override fun toString(): String{
            return "Http2(enabled=$enabled)"
        }
    }

    override fun toString(): String{
        return "ServerContainerConfigure(http=$http, http2=$http2)"
    }


}