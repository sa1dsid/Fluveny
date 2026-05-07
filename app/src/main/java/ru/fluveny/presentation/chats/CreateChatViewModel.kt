package ru.fluveny.presentation.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.fluveny.domain.model.Chat
import ru.fluveny.domain.model.Resource
import ru.fluveny.domain.usecase.CreateChatUseCase

data class CreateChatUiState(
    val isLoading: Boolean = false,
    val createdChat: Chat? = null,
    val error: String? = null
)

class CreateChatViewModel(private val createChatUseCase: CreateChatUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateChatUiState())
    val uiState: StateFlow<CreateChatUiState> = _uiState.asStateFlow()

    fun create(language: String, name: String, type: String, description: String) {
        if (name.isBlank()) {
            _uiState.value = CreateChatUiState(error = "Введите название чата")
            return
        }
        viewModelScope.launch {
            _uiState.value = CreateChatUiState(isLoading = true)
            _uiState.value = when (val result = createChatUseCase(language, name.trim(), type, description.trim())) {
                is Resource.Error -> CreateChatUiState(error = result.message)
                Resource.Loading -> CreateChatUiState(isLoading = true)
                is Resource.Success -> CreateChatUiState(createdChat = result.data)
            }
        }
    }
}
