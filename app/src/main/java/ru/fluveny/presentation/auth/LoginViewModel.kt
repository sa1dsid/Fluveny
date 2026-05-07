package ru.fluveny.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.fluveny.data.token.TokenStorage
import ru.fluveny.domain.model.Resource
import ru.fluveny.domain.usecase.LoginUseCase

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false,
    val hasSavedSession: Boolean = false
)

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val tokenStorage: TokenStorage
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            tokenStorage.isAuthorizedFlow.collect { authorized ->
                _uiState.value = _uiState.value.copy(hasSavedSession = authorized)
            }
        }
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Введите email и пароль")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            _uiState.value = when (val result = loginUseCase(email.trim(), password)) {
                is Resource.Error -> LoginUiState(error = result.message)
                Resource.Loading -> LoginUiState(isLoading = true)
                is Resource.Success -> LoginUiState(isLoggedIn = true, hasSavedSession = true)
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
