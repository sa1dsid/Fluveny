package ru.fluveny.domain.usecase

import ru.fluveny.domain.repository.MessageRepository

class GetMessagesUseCase(private val repository: MessageRepository) {
    suspend operator fun invoke(chatId: Long, page: Int = 0, size: Int = 20) =
        repository.getMessages(chatId, page, size)
}

class SendMessageUseCase(private val repository: MessageRepository) {
    suspend operator fun invoke(chatId: Long, message: String) = repository.sendMessage(chatId, message)
}

class CheckMessageUseCase(private val repository: MessageRepository) {
    suspend operator fun invoke(messageId: Long) = repository.checkMessage(messageId)
}

class GetMessagesCountUseCase(private val repository: MessageRepository) {
    suspend operator fun invoke() = repository.getMessagesCount()
}
