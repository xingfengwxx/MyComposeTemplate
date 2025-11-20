package com.wangxingxing.mycomposeapp.aop.permission

/**
 * 权限请求注解
 * @param permissions 需要请求的权限，请使用 [PermissionNames] 中的常量
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
annotation class Permissions(vararg val value: String)
