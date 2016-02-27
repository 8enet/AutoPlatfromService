package com.zzzmode.platfrom.proxyserver

import org.springframework.context.annotation.Import

import java.lang.annotation.*

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Documented
@Import(HttpProxyAutoConfiguration::class)
annotation class EnableHttpProxy