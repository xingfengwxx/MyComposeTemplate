package com.wangxingxing.mycomposeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wangxingxing.mycomposeapp.model.User
import com.wangxingxing.mycomposeapp.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()
    
    fun loadUser(userId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            userRepository.getUserById(userId)
                .collect { result ->
                    result.fold(
                        onSuccess = { user ->
                            _uiState.value = _uiState.value.copy(
                                user = user,
                                isLoading = false,
                                errorMessage = if (user == null) "用户未找到" else null
                            )
                        },
                        onFailure = { e ->
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                errorMessage = "加载用户详情失败: ${e.message}"
                            )
                        }
                    )
                }
        }
    }
}

