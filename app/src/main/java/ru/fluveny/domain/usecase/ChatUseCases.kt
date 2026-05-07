package ru.fluveny.domain.usecase

import ru.fluveny.domain.repository.ChatRepository

class GetChatsUseCase(private val repository: ChatRepository) {
    suspend operator fun invoke() = repository.getChats()
}

class CreateChatUseCase(private val repository: ChatRepository) {
    suspend operator fun invoke(language: String, name: String, type: String, description: String) =
        repository.createChat(language, name, type, description)
}
