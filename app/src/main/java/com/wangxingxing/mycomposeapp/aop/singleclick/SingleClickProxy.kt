package com.wangxingxing.mycomposeapp.aop.singleclick

import com.blankj.utilcode.util.LogUtils
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * 方案 4：使用 Java 动态代理实现 AOP 防抖
 * 
 * 优点：
 * - 真正的 AOP 实现
 * - 可以拦截所有方法调用
 * - 支持注解驱动
 * 
 * 缺点：
 * - 需要接口
 * - 有反射开销
 * - 代码较复杂
 * 
 * 使用方式：
 * ```kotlin
 * interface ClickHandler {
 *     fun onClick()
 * }
 * 
 * val handler = SingleClickProxy.create(ClickHandler::class.java, object : ClickHandler {
 *     override fun onClick() {
 *         // 业务逻辑
 *     }
 * }, 1000L)
 * 
 * handler.onClick() // 会被防抖拦截
 * ```
 */
class SingleClickProxy(
    private val target: Any,
    private val interval: Long = 1000L
) : InvocationHandler {
    
    private val lastClickTimes = mutableMapOf<String, Long>()
    
    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
        // 检查方法是否有 @SingleClick 注解
        val annotation = method?.getAnnotation(SingleClick::class.java)
        val clickInterval = annotation?.value ?: interval
        
        val methodKey = method?.name ?: "unknown"
        val currentTime = System.currentTimeMillis()
        val lastTime = lastClickTimes[methodKey] ?: 0L
        
        return if (currentTime - lastTime >= clickInterval) {
            lastClickTimes[methodKey] = currentTime
            method?.invoke(target, *(args ?: emptyArray()))
        } else {
            null // 或者返回默认值
        }
    }
    
    companion object {
        /**
         * 创建防抖代理对象
         */
        @Suppress("UNCHECKED_CAST")
        fun <T> create(
            clazz: Class<T>,
            target: T,
            interval: Long = 1000L
        ): T {
            return Proxy.newProxyInstance(
                clazz.classLoader,
                arrayOf(clazz),
                SingleClickProxy(target as Any, interval)
            ) as T
        }
    }
}

