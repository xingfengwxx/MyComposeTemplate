package com.wangxingxing.mycomposeapp.viewmodel

import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * SettingsViewModel
 * 
 * 注意：防抖功能在 UI 层使用方案3（Compose Composable）实现，
 * ViewModel 只负责业务逻辑，不处理防抖。
 */
@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    /**
     * 请求文件读写权限。
     * 注意：权限请求在 UI 层处理，此方法只负责权限授予后的业务逻辑。
     * 防抖在 UI 层通过 rememberSingleClick 实现。
     */
    fun requestFilePermissions() {
        val message = "文件权限请求成功！"
        LogUtils.i(message)
        ToastUtils.showShort(message)
    }

    /**
     * 单独测试快速点击。
     * 防抖在 UI 层通过 rememberSingleClick 实现。
     */
    fun testSingleClick() {
        val message = "测试防抖点击成功！"
        LogUtils.i(message)
        ToastUtils.showShort(message)
    }
}
