package com.wangxingxing.mycomposeapp.network.model

import com.google.gson.annotations.SerializedName

/**
 * 登录响应数据模型
 */
data class LoginResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("token")
    val token: String? = null,
    @SerializedName("user")
    val user: UserResponse? = null
)

