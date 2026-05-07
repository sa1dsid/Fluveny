package ru.fluveny.domain.repository

import ru.fluveny.domain.model.Chat
import ru.fluveny.domain.model.Resource

interface ChatRepository {
    suspend fun getChats(): Resource<List<Chat>>
    suspend fun createChat(language: String, name: String, type: String, description: String): Resource<Chat>
}
