package com.wangxingxing.mycomposeapp.repository

import com.wangxingxing.mycomposeapp.model.User
import com.wangxingxing.mycomposeapp.model.UserInfo
import com.wangxingxing.mycomposeapp.data.UserLocalDataSource
import com.wangxingxing.mycomposeapp.network.RemoteDataSource
import com.wangxingxing.mycomposeapp.network.toNetworkException
import com.wangxingxing.mycomposeapp.utils.LogUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 用户数据仓库
 * 负责协调远程数据源和本地数据源
 */
@Singleton
class UserRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource
) {
    
    /**
     * 登录
     * @return Flow<Result<Boolean>> 登录结果
     */
    fun login(email: String, password: String): Flow<Result<Boolean>> = flow {
        val result = remoteDataSource.login(email, password)
        result.fold(
            onSuccess = { response ->
                // 登录成功，保存用户信息到本地
                userLocalDataSource.saveUserInfo(
                    UserInfo(
                        id = response.id,
                        username = response.username,
                        email = response.email,
                        token = response.token
                    )
                )
                LogUtils.d("登录成功并已保存用户信息: $response")
                emit(Result.success(true))
            },
            onFailure = { e ->
                emit(Result.failure(e.toNetworkException()))
            }
        )
    }
    
    /**
     * 获取用户列表
     * @return Flow<Result<List<User>>>
     */
    fun getUsers(): Flow<Result<List<User>>> = flow {
        val result = remoteDataSource.getUsers()
        result.fold(
            onSuccess = { users ->
                emit(Result.success(users))
            },
            onFailure = { e ->
                emit(Result.failure(e.toNetworkException()))
            }
        )
    }
    
    /**
     * 根据ID获取用户详情
     * @return Flow<Result<User?>>
     */
    fun getUserById(id: Int): Flow<Result<User?>> = flow {
        val result = remoteDataSource.getUserById(id)
        result.fold(
            onSuccess = { user ->
                emit(Result.success(user))
            },
            onFailure = { e ->
                emit(Result.failure(e.toNetworkException()))
            }
        )
    }
    
    /**
     * 获取当前登录用户
     * @return Flow<Result<User?>>
     */
    fun getCurrentUser(): Flow<Result<UserInfo?>> = flow {
        userLocalDataSource.getUserForUi()
            .collect { user ->
                emit(Result.success(user))
            }
    }
}

