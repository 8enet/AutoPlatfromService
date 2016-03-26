package com.zzzmode.platfrom.proxyserver

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

/**
 * 启动http代理配置
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@kotlin.annotation.MustBeDocumented
@Import(HttpProxyAutoConfiguration::class)
@Configuration
annotation class EnableHttpProxy