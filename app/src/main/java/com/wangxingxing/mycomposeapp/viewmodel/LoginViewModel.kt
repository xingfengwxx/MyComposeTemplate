package com.wangxingxing.mycomposeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wangxingxing.mycomposeapp.repository.UserDataRepository
import com.wangxingxing.mycomposeapp.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSuccessful: Boolean = false,
    val autoLoginAttempted: Boolean = false // 新增状态，防止重复自动登录
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userDataRepository: UserDataRepository // 注入 UserDataRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(username = email, errorMessage = null)
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password, errorMessage = null)
    }

    fun login() {
        val currentState = _uiState.value

        // Validation
        if (currentState.username.isBlank()) {
            _uiState.value = currentState.copy(errorMessage = "Username cannot be empty")
            return
        }

        if (currentState.password.isBlank()) {
            _uiState.value = currentState.copy(errorMessage = "Password cannot be empty")
            return
        }

        if (currentState.password.length < 6) {
            _uiState.value = currentState.copy(errorMessage = "Password must be at least 6 characters")
            return
        }

        viewModelScope.launch {
            _uiState.value = currentState.copy(isLoading = true, errorMessage = null)

            userRepository.login(currentState.username, currentState.password)
                .collect { result ->
                    result.fold(
                        onSuccess = { success ->
                            if (success) {
                                // 登录成功，保存凭据
                                userDataRepository.saveCredentials(currentState.username, currentState.password)
                            }
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                isLoginSuccessful = success,
                                errorMessage = if (!success) "登录失败" else null
                            )
                        },
                        onFailure = { e ->
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                isLoginSuccessful = false,
                                errorMessage = "登录失败: ${e.message}"
                            )
                        }
                    )
                }
        }
    }

    /**
     * 尝试自动登录
     */
    fun attemptAutoLogin() {
        viewModelScope.launch {
            // 只尝试一次自动登录
            if (_uiState.value.autoLoginAttempted) return@launch

            val credentials = userDataRepository.savedCredentials.first() // 获取一次性的凭据
            _uiState.value = _uiState.value.copy(autoLoginAttempted = true)

            if (credentials != null) {
                val (username, password) = credentials
                // 使用保存的凭据更新UI状态并调用登录
                _uiState.value = _uiState.value.copy(username = username, password = password)
                login()
            }
        }
    }

    fun resetLoginState() {
        _uiState.value = _uiState.value.copy(isLoginSuccessful = false)
    }
}
