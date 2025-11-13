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

data class HomeUiState(
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        loadUsers()
    }
    
    fun loadUsers() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            userRepository.getUsers()
                .collect { result ->
                    result.fold(
                        onSuccess = { users ->
                            _uiState.value = _uiState.value.copy(
                                users = users,
                                isLoading = false
                            )
                        },
                        onFailure = { e ->
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                errorMessage = "加载用户列表失败: ${e.message}"
                            )
                        }
                    )
                }
        }
    }
}

