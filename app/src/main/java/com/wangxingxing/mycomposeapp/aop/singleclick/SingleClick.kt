package com.wangxingxing.mycomposeapp.aop.singleclick

/**
 * 防抖点击注解
 * @param value 防抖间隔时间（毫秒），默认 1000ms
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class SingleClick(val value: Long = 1000)

