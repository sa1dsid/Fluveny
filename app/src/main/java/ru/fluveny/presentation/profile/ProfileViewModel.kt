package ru.fluveny.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.fluveny.domain.model.Resource
import ru.fluveny.domain.model.UserProfile
import ru.fluveny.domain.usecase.ChangeEmailUseCase
import ru.fluveny.domain.usecase.ChangePasswordUseCase
import ru.fluveny.domain.usecase.GetAccountUseCase
import ru.fluveny.domain.usecase.LogoutUseCase

data class ProfileUiState(
    val isLoading: Boolean = false,
    val profile: UserProfile? = null,
    val message: String? = null,
    val error: String? = null,
    val isLoggedOut: Boolean = false
)

class ProfileViewModel(
    private val getAccountUseCase: GetAccountUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val changeEmailUseCase: ChangeEmailUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun load() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState(isLoading = true)
            _uiState.value = when (val result = getAccountUseCase()) {
                is Resource.Error -> ProfileUiState(error = result.message)
                Resource.Loading -> ProfileUiState(isLoading = true)
                is Resource.Success -> ProfileUiState(profile = result.data)
            }
        }
    }

    fun changePassword(currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, message = null)
            _uiState.value = when (val result = changePasswordUseCase(currentPassword, newPassword)) {
                is Resource.Error -> _uiState.value.copy(isLoading = false, error = result.message)
                Resource.Loading -> _uiState.value.copy(isLoading = true)
                is Resource.Success -> _uiState.value.copy(isLoading = false, message = "Пароль обновлен")
            }
        }
    }

    fun changeEmail(email: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, message = null)
            _uiState.value = when (val result = changeEmailUseCase(email.trim())) {
                is Resource.Error -> _uiState.value.copy(isLoading = false, error = result.message)
                Resource.Loading -> _uiState.value.copy(isLoading = true)
                is Resource.Success -> _uiState.value.copy(isLoading = false, message = "Email обновлен")
            }
            load()
        }
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            logoutUseCase()
            _uiState.value = ProfileUiState(isLoggedOut = true)
        }
    }
}
