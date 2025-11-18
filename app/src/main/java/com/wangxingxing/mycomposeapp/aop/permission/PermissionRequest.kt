package com.wangxingxing.mycomposeapp.aop.permission

import com.hjq.permissions.permission.base.IPermission
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class PermissionRequest(val permissions: Array<KClass<out IPermission>>)
