package com.wangxingxing.mycomposeapp.network.model

import com.google.gson.annotations.SerializedName

/**
 * 登录请求数据模型
 */
data class LoginRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)

