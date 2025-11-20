package com.wangxingxing.mycomposeapp.utils

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.hjq.permissions.permission.base.IPermission
import com.blankj.utilcode.util.ToastUtils

/**
 * 权限请求工具类
 * 用于在 Compose 中请求权限
 */
object PermissionUtils {

    /**
     * 检查权限是否已授予
     */
    fun isGranted(permissions: Array<IPermission>): Boolean {
        return XXPermissions.isGrantedPermissions(ActivityUtils.getTopActivity(), permissions)
    }
    
    /**
     * 请求权限
     * @param activity Activity 上下文
     * @param permissions 需要请求的权限列表
     * @param onGranted 所有权限授予后的回调
     * @param onDenied 权限被拒绝后的回调（可选）
     */
    fun requestPermissions(
        permissions: Array<IPermission>,
        onGranted: () -> Unit,
        onDenied: (() -> Unit)? = null
    ) {
        // 先检查是否已授予
        if (isGranted(permissions)) {
            onGranted()
            return
        }

        val activity = ActivityUtils.getTopActivity()
        
        // 请求权限
        XXPermissions.with(activity)
            .permissions(permissions)
            .request { grantedList, deniedList ->
                val allGranted = deniedList.isEmpty()
                if (allGranted) {
                    onGranted()
                } else {
                    // 判断是否永久拒绝
                    val doNotAskAgain = XXPermissions.isDoNotAskAgainPermissions(
                        activity,
                        deniedList
                    )
                    if (doNotAskAgain) {
                        ToastUtils.showShort("永久拒绝授权，请手动授予权限")
                    } else {
                        ToastUtils.showShort("部分权限未授予")
                    }
                    onDenied?.invoke()
                }
            }
    }
}

/**
 * Compose 中获取 Activity
 */
@Composable
fun getActivity(): Activity? {
    val context = LocalContext.current
    return context as? Activity
}

