package com.wangxingxing.mycomposeapp.aop.singleclick

import androidx.compose.runtime.*
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.delay

/**
 * 方案 3：Compose 专用的防抖 Composable
 * 
 * 优点：
 * - 完全符合 Compose 设计理念
 * - 自动管理状态和生命周期
 * - 使用简单，声明式 API
 * 
 * 使用方式：
 * ```kotlin
 * val onClick = rememberSingleClick(1000L) {
 *     // 执行业务逻辑
 * }
 * 
 * Button(onClick = onClick) {
 *     Text("Click Me")
 * }
 * ```
 */

/**
 * 记住一个防抖的点击处理函数
 * @param interval 防抖间隔（毫秒），默认 1000ms
 * @param onClick 点击时要执行的函数
 * @return 防抖后的点击处理函数
 */
@Composable
fun rememberSingleClick(
    interval: Long = 1000L,
    onClick: () -> Unit
): () -> Unit {
    var lastClickTime by remember { mutableLongStateOf(0L) }
    
    return {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime >= interval) {
            lastClickTime = currentTime
            onClick()
        }
    }
}


