package com.wangxingxing.mycomposeapp.network.model

import com.google.gson.annotations.SerializedName
import com.wangxingxing.mycomposeapp.model.User

/**
 * 用户响应数据模型
 */
data class UserResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String
) {
    /**
     * 转换为领域模型
     */
    fun toDomain(): User {
        return User(
            id = id,
            name = name,
            email = email
        )
    }
}

