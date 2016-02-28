package com.zzzmode.platfrom.bean

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Created by zl on 16/2/16.
 */
@ConfigurationProperties(prefix = "zzzmode.proxyserver")
open  class HttpProxyProperties(
        var port: Int = 8099,
        var x509Path: String? = null,
        var pemPath: String? = null,
        var password: String? = null
) {

    
    override fun toString(): String {
        return "NettyHttpProxy(port=$port, x509Path=$x509Path, pemPath=$pemPath, password=$password)"
    }
}
