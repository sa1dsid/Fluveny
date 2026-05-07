package ru.fluveny.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.fluveny.domain.model.CorrectionResult
import ru.fluveny.domain.model.Message
import ru.fluveny.domain.model.Resource
import ru.fluveny.domain.usecase.CheckMessageUseCase
import ru.fluveny.domain.usecase.GetMessagesUseCase
import ru.fluveny.domain.usecase.SendMessageUseCase

data class ChatUiState(
    val isLoading: Boolean = false,
    val isSending: Boolean = false,
    val messages: List<Message> = emptyList(),
    val correction: CorrectionResult? = null,
    val error: String? = null
)

class ChatViewModel(
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val checkMessageUseCase: CheckMessageUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun load(chatId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            _uiState.value = when (val result = getMessagesUseCase(chatId)) {
                is Resource.Error -> _uiState.value.copy(isLoading = false, error = result.message)
                Resource.Loading -> _uiState.value.copy(isLoading = true)
                is Resource.Success -> _uiState.value.copy(isLoading = false, messages = result.data)
            }
        }
    }

    fun send(chatId: Long, text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSending = true, error = null)
            _uiState.value = when (val result = sendMessageUseCase(chatId, text.trim())) {
                is Resource.Error -> _uiState.value.copy(isSending = false, error = result.message)
                Resource.Loading -> _uiState.value.copy(isSending = true)
                is Resource.Success -> _uiState.value.copy(
                    isSending = false,
                    messages = _uiState.value.messages + result.data
                )
            }
            load(chatId)
        }
    }

    fun check(messageId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(error = null, correction = null)
            _uiState.value = when (val result = checkMessageUseCase(messageId)) {
                is Resource.Error -> _uiState.value.copy(error = result.message)
                Resource.Loading -> _uiState.value
                is Resource.Success -> _uiState.value.copy(correction = result.data)
            }
        }
    }

    fun correctionShown() {
        _uiState.value = _uiState.value.copy(correction = null)
    }
}
