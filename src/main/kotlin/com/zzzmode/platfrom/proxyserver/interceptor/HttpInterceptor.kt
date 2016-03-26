package com.zzzmode.platfrom.proxyserver

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

/**
 * 拦截注解
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@kotlin.annotation.MustBeDocumented
@Import(HttpInterceptorConfiguration::class)
@Configuration
annotation class HttpInterceptor