package com.wangxingxing.mycomposeapp.aop.singleclick

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 方案 5：ViewModel 专用的防抖基类
 * 
 * 优点：
 * - 专门为 ViewModel 设计
 * - 使用 StateFlow，响应式
 * - 类型安全
 * 
 * 使用方式：
 * ```kotlin
 * class MyViewModel : SingleClickViewModel() {
 *     fun onClick() {
 *         executeWithSingleClick("onClick", 1000L) {
 *             // 执行业务逻辑
 *         }
 *     }
 * }
 * ```
 */
abstract class SingleClickViewModel : ViewModel() {
    
    private val _clickStates = MutableStateFlow<Map<String, Long>>(emptyMap())
    val clickStates: StateFlow<Map<String, Long>> = _clickStates.asStateFlow()
    
    /**
     * 执行带防抖的函数
     * @param key 唯一标识符，用于区分不同的点击事件
     * @param interval 防抖间隔（毫秒）
     * @param action 要执行的函数
     */
    protected fun executeWithSingleClick(
        key: String,
        interval: Long = 1000L,
        action: () -> Unit
    ) {
        val currentTime = System.currentTimeMillis()
        val lastTime = _clickStates.value[key] ?: 0L
        
        if (currentTime - lastTime >= interval) {
            _clickStates.value = _clickStates.value + (key to currentTime)
            action()
        }
    }
    
    /**
     * 检查是否可以点击
     */
    protected fun canClick(key: String, interval: Long = 1000L): Boolean {
        val currentTime = System.currentTimeMillis()
        val lastTime = _clickStates.value[key] ?: 0L
        return currentTime - lastTime >= interval
    }
    
    /**
     * 重置点击状态
     */
    protected fun resetClickState(key: String) {
        _clickStates.value = _clickStates.value - key
    }
    
    /**
     * 清除所有点击状态
     */
    protected fun clearAllClickStates() {
        _clickStates.value = emptyMap()
    }
}

