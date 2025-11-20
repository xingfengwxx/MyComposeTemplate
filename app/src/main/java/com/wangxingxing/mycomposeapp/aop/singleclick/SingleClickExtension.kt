package com.wangxingxing.mycomposeapp.aop.singleclick

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * 方案 2：使用扩展函数 + 高阶函数实现防抖
 * 
 * 优点：
 * - 使用简单，一行代码即可
 * - 支持协程，线程安全
 * - 可以自定义防抖逻辑
 * 
 * 使用方式：
 * ```kotlin
 * Button(onClick = {
 *     onClick.withSingleClick(1000L) {
 *         // 执行业务逻辑
 *     }
 * })
 * ```
 */

// 使用 ThreadLocal 存储上次点击时间（适用于非协程环境）
private val clickTimes = mutableMapOf<String, Long>()
private val mutex = Mutex()

/**
 * 为函数添加防抖功能
 * @param interval 防抖间隔（毫秒）
 * @param action 要执行的函数
 */
suspend fun (() -> Unit).withSingleClick(
    interval: Long = 1000L,
    action: () -> Unit
) {
    val key = this.toString()
    mutex.withLock {
        val currentTime = System.currentTimeMillis()
        val lastTime = clickTimes[key] ?: 0L
        
        if (currentTime - lastTime >= interval) {
            clickTimes[key] = currentTime
            action()
        }
    }
}

/**
 * 同步版本的防抖函数（使用 ThreadLocal）
 */
private val threadLocalClickTimes = ThreadLocal<MutableMap<String, Long>>().apply {
    set(mutableMapOf())
}

/**
 * 为函数添加防抖功能（同步版本）
 * @param interval 防抖间隔（毫秒）
 * @param action 要执行的函数
 */
fun (() -> Unit).withSingleClickSync(
    interval: Long = 1000L,
    action: () -> Unit
) {
    val key = this.toString()
    val clickTimes = threadLocalClickTimes.get()
    val currentTime = System.currentTimeMillis()
    val lastTime = clickTimes[key] ?: 0L
    
    if (currentTime - lastTime >= interval) {
        clickTimes[key] = currentTime
        action()
    }
}

/**
 * 为 ViewModel 方法添加防抖的扩展函数
 */
fun <T> T.withSingleClick(
    interval: Long = 1000L,
    key: String = "",
    action: T.() -> Unit
) {
    val clickKey = if (key.isNotEmpty()) key else this.toString()
    val clickTimes = threadLocalClickTimes.get()
    val currentTime = System.currentTimeMillis()
    val lastTime = clickTimes[clickKey] ?: 0L
    
    if (currentTime - lastTime >= interval) {
        clickTimes[clickKey] = currentTime
        action()
    }
}

