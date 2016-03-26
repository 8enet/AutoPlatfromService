package com.zzzmode.platfrom.services

import com.zzzmode.platfrom.bean.HttpProxyProperties
import com.zzzmode.platfrom.proxyserver.ProxyServer
import com.zzzmode.platfrom.proxyserver.interceptor.OnHttpInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.ComponentScan
import org.springframework.stereotype.Service
import org.springframework.util.SocketUtils
import java.util.concurrent.ConcurrentHashMap
import javax.annotation.PreDestroy

/**
 * 代理服务,核心服务
 * Created by zl on 16/2/27.
 */
@Service
@ComponentScan
open class HttpProxyService {


    @Value("\${zzzmode.proxyserver.port}")
    var minPort:Int?=30000

    @Autowired
    var interceptores: List<OnHttpInterceptor>? = null

    @Autowired(required = false)
    val properties: HttpProxyProperties? = null

    private  val proxyServersMap = ConcurrentHashMap<Int,ProxyServer>()

    /**
     * 启动代理
     */
    fun startProxyServer(port:Int){

        properties?.apply {
            val prop= copyProperties(port)

            val server=ProxyServer(prop)
            server.newServer()
            server.addFilter(interceptores)
            server.start()
            proxyServersMap.put(port,server)
        }

    }


    /**
     * 关闭代理
     */
    fun stopProxyServer(port: Int){
        proxyServersMap.get(port)?.apply {
            this.stop()

            proxyServersMap.remove(port)
        }
    }

    fun getNextPort():Int{
        var port= SocketUtils.findAvailableTcpPort(minPort!!)
        return port;
    }

    /**
     * 容器结束,关闭所有代理
     */
    @PreDestroy
    fun close() {
        proxyServersMap.forEach { i, proxyServer -> stopProxyServer(i) }
    }

}