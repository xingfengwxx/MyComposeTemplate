package com.wangxingxing.mycomposeapp.network

import java.io.IOException

/**
 * 网络异常封装
 */
sealed class NetworkException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    
    /**
     * 网络连接异常
     */
    class NetworkError(cause: Throwable) : NetworkException("网络连接失败", cause)
    
    /**
     * HTTP错误
     */
    class HttpError(val code: Int, message: String) : NetworkException("HTTP错误: $code - $message")
    
    /**
     * 未登录/未授权
     */
    class Unauthenticated(message: String = "未登录或登录已过期") : NetworkException(message)
    
    /**
     * 解析错误
     */
    class ParseError(cause: Throwable) : NetworkException("数据解析失败", cause)
    
    /**
     * 未知错误
     */
    class UnknownError(cause: Throwable) : NetworkException("未知错误", cause)
}

/**
 * 异常处理扩展函数
 */
fun Throwable.toNetworkException(): NetworkException {
    return when (this) {
        is IOException -> NetworkException.NetworkError(this)
        is retrofit2.HttpException -> {
            NetworkException.HttpError(
                code = this.code(),
                message = this.message()
            )
        }
        is com.google.gson.JsonSyntaxException -> NetworkException.ParseError(this)
        is NetworkException -> this
        else -> NetworkException.UnknownError(this)
    }
}

