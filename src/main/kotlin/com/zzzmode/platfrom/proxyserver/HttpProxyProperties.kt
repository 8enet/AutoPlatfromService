package com.zzzmode.platfrom.bean

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

/**
 * Created by zl on 16/2/16.
 */
@ConfigurationProperties(prefix = "zzzmode.proxyserver")
open class HttpProxyProperties(
        var port: Int = 8099,
        var x509Path: String? = null,
        var pemPath: String? = null,
        var password: String? = null
) {


    /**
     * copy properties
     */
    fun copyProperties(newPort:Int):HttpProxyProperties{
        return HttpProxyProperties(newPort,x509Path,pemPath,password)
    }

    override fun toString(): String {
        return "HttpProxyProperties(port=$port, x509Path=$x509Path, pemPath=$pemPath, password=$password)"
    }
}
