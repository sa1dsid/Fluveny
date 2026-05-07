package ru.fluveny.presentation.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.fluveny.domain.model.Chat
import ru.fluveny.domain.model.Resource
import ru.fluveny.domain.usecase.GetChatsUseCase

data class ChatsUiState(
    val isLoading: Boolean = false,
    val chats: List<Chat> = emptyList(),
    val error: String? = null
)

class ChatsViewModel(private val getChatsUseCase: GetChatsUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(ChatsUiState())
    val uiState: StateFlow<ChatsUiState> = _uiState.asStateFlow()

    fun load() {
        viewModelScope.launch {
            _uiState.value = ChatsUiState(isLoading = true)
            _uiState.value = when (val result = getChatsUseCase()) {
                is Resource.Error -> ChatsUiState(error = result.message)
                Resource.Loading -> ChatsUiState(isLoading = true)
                is Resource.Success -> ChatsUiState(chats = result.data)
            }
        }
    }
}
