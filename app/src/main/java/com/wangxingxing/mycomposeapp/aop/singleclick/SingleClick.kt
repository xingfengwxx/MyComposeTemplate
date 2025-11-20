package com.wangxingxing.mycomposeapp.aop.singleclick

/**
 * 防止快速点击的注解
 * @param value 两次点击之间的最小间隔，单位毫秒
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class SingleClick(val value: Long = 1000)
