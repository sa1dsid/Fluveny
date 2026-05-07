package ru.fluveny.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.fluveny.domain.model.Resource
import ru.fluveny.domain.usecase.RegisterUseCase

data class RegisterUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRegistered: Boolean = false
)

class RegisterViewModel(private val registerUseCase: RegisterUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun register(username: String, email: String, password: String, language: String) {
        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            _uiState.value = RegisterUiState(error = "Заполните все поля")
            return
        }
        viewModelScope.launch {
            _uiState.value = RegisterUiState(isLoading = true)
            _uiState.value = when (val result = registerUseCase(username.trim(), email.trim(), password, language)) {
                is Resource.Error -> RegisterUiState(error = result.message)
                Resource.Loading -> RegisterUiState(isLoading = true)
                is Resource.Success -> RegisterUiState(isRegistered = true)
            }
        }
    }
}
