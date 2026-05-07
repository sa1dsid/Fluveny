package ru.fluveny.domain.repository

import ru.fluveny.domain.model.CorrectionResult
import ru.fluveny.domain.model.Message
import ru.fluveny.domain.model.Resource

interface MessageRepository {
    suspend fun getMessages(chatId: Long, page: Int = 0, size: Int = 20): Resource<List<Message>>
    suspend fun sendMessage(chatId: Long, message: String): Resource<Message>
    suspend fun checkMessage(messageId: Long): Resource<CorrectionResult>
    suspend fun getMessagesCount(): Resource<Int>
}
