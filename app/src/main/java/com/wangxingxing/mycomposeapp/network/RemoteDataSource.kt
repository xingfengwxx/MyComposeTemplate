package com.wangxingxing.mycomposeapp.network

import com.wangxingxing.mycomposeapp.model.User
import com.wangxingxing.mycomposeapp.model.UserInfo
import com.wangxingxing.mycomposeapp.network.model.LoginRequest
import com.wangxingxing.mycomposeapp.network.model.LoginResponse
import com.wangxingxing.mycomposeapp.network.model.toResult
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 远程数据源 - 负责网络请求
 * 使用Flow包装网络请求，支持响应式编程
 */
@Singleton
class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    
    /**
     * 登录
     */
    suspend fun login(username: String, password: String): Result<UserInfo> {
        return try {
            val response = apiService.login(username, password)
            response.toResult()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * 获取用户列表
     */
    suspend fun getUsers(): Result<List<User>> {
        return try {
            val response = apiService.getUsers()
            response
                .toResult()
                .map { list -> list.map { it.toDomain() } }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * 根据ID获取用户详情
     */
    suspend fun getUserById(id: Int): Result<User> {
        return try {
            val response = apiService.getUserById(id)
            response
                .toResult()
                .map { it.toDomain() }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * 获取当前登录用户
     */
    suspend fun getCurrentUser(): Result<User> {
        return try {
            val response = apiService.getCurrentUser()
            response
                .toResult()
                .map { it.toDomain() }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

