package com.zzzmode.platfrom.proxyserver

import net.lightbody.bmp.BrowserMobProxy

/**
 * Created by zl on 16/2/27.
 */
interface HttpProxyConfigurer {
    /**
     * 启动代理前的回调
     */
    fun onConfiguration(proxy: BrowserMobProxy)
}