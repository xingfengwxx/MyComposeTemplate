package com.wangxingxing.mycomposeapp.network.model

import com.google.gson.annotations.SerializedName
import com.wangxingxing.mycomposeapp.network.NetworkException

/**
 * 统一返回数据结构
 *
 * {
 *   "data": ...,
 *   "errorCode": 0,
 *   "errorMsg": ""
 * }
 */
data class ApiResponse<T>(
    @SerializedName("data")
    val data: T?,
    @SerializedName("errorCode")
    val errorCode: Int,
    @SerializedName("errorMsg")
    val errorMsg: String?
)

/**
 * 将统一响应转换为 Result<T>
 * 规则：
 * - errorCode == 0 视为成功，data 可能为 null，按业务决定是否允许
 * - errorCode == -1001 未登录
 * - 其他非 0 均视为错误
 */
inline fun <reified T> ApiResponse<T>.toResult(): Result<T> {
    return when (errorCode) {
        0 -> {
            if (data != null) {
                Result.success(data)
            } else {
                Result.failure(
                    NetworkException.ParseError(
                        IllegalStateException("返回数据为空")
                    )
                )
            }
        }
        -1001 -> Result.failure(NetworkException.Unauthenticated(errorMsg ?: "未登录"))
        else -> Result.failure(NetworkException.HttpError(errorCode, errorMsg ?: "请求失败"))
    }
}


