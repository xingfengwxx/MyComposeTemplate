package com.wangxingxing.mycomposeapp.network

import com.wangxingxing.mycomposeapp.model.UserInfo
import com.wangxingxing.mycomposeapp.network.model.ApiResponse
import com.wangxingxing.mycomposeapp.network.model.LoginRequest
import com.wangxingxing.mycomposeapp.network.model.LoginResponse
import com.wangxingxing.mycomposeapp.network.model.UserResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {


    /** 登录 */
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") pwd: String
    ): ApiResponse<UserInfo>
    
    /**
     * 获取用户列表
     */
    @GET("users")
    suspend fun getUsers(): ApiResponse<List<UserResponse>>
    
    /**
     * 根据ID获取用户详情
     */
    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): ApiResponse<UserResponse>
    
    /**
     * 获取当前登录用户信息
     */
    @GET("user/me")
    suspend fun getCurrentUser(): ApiResponse<UserResponse>
}

