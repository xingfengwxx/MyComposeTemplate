package com.wangxingxing.mycomposeapp.viewmodel

import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.wangxingxing.mycomposeapp.aop.singleclick.SingleClick
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    /**
     * 请求文件读写权限。
     * 注意：权限请求现在在 UI 层处理，此方法只负责权限授予后的业务逻辑。
     * 使用 @SingleClick 防止快速点击。
     */
    @SingleClick // 使用默认的 1000ms 防抖间隔
    fun requestFilePermissions() {
        // 这里的代码只会在权限全部被授予后，且距离上次点击超过 1 秒后才会执行
        val message = "文件权限请求成功，且无快速点击！"
        LogUtils.i(message)
        ToastUtils.showShort(message)
    }

    /**
     * 单独测试快速点击。
     */
    @SingleClick(value = 2000) // 设置为 2 秒的防抖间隔
    fun testSingleClick() {
        val message = "测试防抖点击成功！"
        LogUtils.i(message)
        ToastUtils.showShort(message)
    }
}
