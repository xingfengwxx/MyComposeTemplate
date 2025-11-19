package com.wangxingxing.mycomposeapp.utils // 你可以放在自己的工具类包下

import com.blankj.utilcode.util.LogUtils
import com.hjq.permissions.permission.PermissionLists
import com.hjq.permissions.permission.base.IPermission
import java.lang.reflect.Field
import java.lang.reflect.Method


/**
 * author : 王星星
 * date : 2025/11/19 10:07
 * email : 1099420259@qq.com
 * description : 权限辅助工具类
 * 用于通过反射获取所有 XXPermissions 定义的权限对象
 */
object PermissionHelper {

    private const val TAG = "PermissionHelper"

    // 使用 lazy 属性，确保反射操作只执行一次，并将结果缓存起来
    val allPermissions: List<IPermission> by lazy {
        val permissions = fetchAllPermissions()
        // 在此处添加校验逻辑
        validatePermissionsCount(permissions.size)
        permissions
    }

    private fun fetchAllPermissions(): List<IPermission> {
        val permissionList = mutableListOf<IPermission>()
        try {
            // 1. 获取 PermissionLists 类的 Class 对象
            val clazz = PermissionLists::class.java

            // 2. 获取该类声明的所有方法
            val methods: Array<Method> = clazz.declaredMethods

            // 3. 遍历所有方法
            for (method in methods) {
                // 4. 设置过滤条件
                val methodName = method.name
                val returnType = method.returnType

                // 条件一：方法名以 "get" 开头
                val startsWithGet = methodName.startsWith("get")
                // 条件二：方法返回类型是 IPermission 接口或其子接口
                val returnsIPermission = IPermission::class.java.isAssignableFrom(returnType)
                // 条件三：方法没有参数
                val hasNoParameters = method.parameterTypes.isEmpty()
                // 条件四：排除指定的方法
                val isNotExcluded = methodName != "getCachePermission" && methodName != "putCachePermission"

                // 5. 如果所有条件都满足，则调用该方法
                if (startsWithGet && returnsIPermission && hasNoParameters && isNotExcluded) {
                    // 调用静态方法。因为 PermissionLists 的方法都是静态的，所以第一个参数传 null
                    val permissionObject = method.invoke(null)
                    if (permissionObject is IPermission) {
                        permissionList.add(permissionObject)
                    }
                }
            }
        } catch (e: Exception) {
            // 在实际应用中，处理可能发生的反射异常
            LogUtils.e(TAG, "Failed to fetch permissions via reflection: ${e.message}")
        }
        return permissionList
    }

    /**
     * 校验反射获取的权限数量与 PermissionLists.PERMISSION_COUNT 是否一致
     */
    private fun validatePermissionsCount(fetchedSize: Int) {
        try {
            // 1. 获取 PermissionLists 类的 Class 对象
            val clazz = PermissionLists::class.java
            // 2. 通过反射获取名为 PERMISSION_COUNT 的字段 (Field)
            val field: Field = clazz.getDeclaredField("PERMISSION_COUNT")
            // 3. 因为字段是静态的，所以 get 方法的参数传 null
            val expectedCount = field.get(null) as? Int

            if (expectedCount == null) {
                LogUtils.w("Could not retrieve PERMISSION_COUNT from PermissionLists.")
                return
            }

            // 4. 比较数量是否一致
            if (fetchedSize != expectedCount) {
                LogUtils.w(
                    "Permission count mismatch! " +
                            "Reflected count: $fetchedSize, " +
                            "Expected count (PERMISSION_COUNT): $expectedCount. " +
                            "Please check if XXPermissions library has been updated."
                )
            } else {
                LogUtils.i("Permission count validation passed. Total permissions: $fetchedSize")
            }

        } catch (e: Exception) {
            LogUtils.e("Failed to validate permissions count via reflection", e)
        }
    }
}
