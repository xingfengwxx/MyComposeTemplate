package com.wangxingxing.mycomposeapp.aop.singleclick

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 方案 1：使用 Kotlin 委托模式实现防抖
 * 
 * 优点：
 * - 无需额外插件，纯 Kotlin 实现
 * - 类型安全，编译时检查
 * - 性能好，无反射开销
 * 
 * 使用方式：
 * ```kotlin
 * class MyViewModel {
 *     private var lastClickTime by SingleClickDelegate(1000L)
 *     
 *     fun onClick() {
 *         if (lastClickTime.canClick()) {
 *             lastClickTime = System.currentTimeMillis()
 *             // 执行业务逻辑
 *         }
 *     }
 * }
 * ```
 */
class SingleClickDelegate(
    private val interval: Long = 1000L
) : ReadWriteProperty<Any?, Long> {
    
    private var lastClickTime: Long = 0L
    
    override fun getValue(thisRef: Any?, property: KProperty<*>): Long {
        return lastClickTime
    }
    
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Long) {
        lastClickTime = value
    }
    
    /**
     * 检查是否可以点击
     */
    fun canClick(): Boolean {
        val currentTime = System.currentTimeMillis()
        return currentTime - lastClickTime >= interval
    }
    
    /**
     * 更新点击时间并返回是否可以点击
     */
    fun tryClick(): Boolean {
        val currentTime = System.currentTimeMillis()
        return if (currentTime - lastClickTime >= interval) {
            lastClickTime = currentTime
            true
        } else {
            false
        }
    }
}

/**
 * 扩展函数：为 Long 类型添加 tryClick 功能
 */
fun Long.tryClick(interval: Long = 1000L): Boolean {
    val currentTime = System.currentTimeMillis()
    return if (currentTime - this >= interval) {
        true
    } else {
        false
    }
}

/**
 * 扩展函数：检查时间戳是否允许点击
 */
fun Long.canClick(interval: Long = 1000L): Boolean {
    return System.currentTimeMillis() - this >= interval
}

