package ru.fluveny.data.repository

import com.google.gson.Gson
import ru.fluveny.data.remote.api.ChatApi
import ru.fluveny.data.remote.dto.ChatDto
import ru.fluveny.data.remote.dto.CreateChatRequestDto
import ru.fluveny.data.remote.mapper.toDomain
import ru.fluveny.data.remote.parseListEnvelope
import ru.fluveny.data.remote.safeApiCall
import ru.fluveny.domain.model.Chat
import ru.fluveny.domain.model.Resource
import ru.fluveny.domain.repository.ChatRepository

class ChatRepositoryImpl(
    private val chatApi: ChatApi,
    private val gson: Gson
) : ChatRepository {
    override suspend fun getChats(): Resource<List<Chat>> {
        return when (val result = safeApiCall { chatApi.getChats() }) {
            is Resource.Error -> result
            Resource.Loading -> Resource.Loading
            is Resource.Success -> Resource.Success(
                gson.parseListEnvelope<ChatDto>(result.data).map(ChatDto::toDomain)
            )
        }
    }

    override suspend fun createChat(
        language: String,
        name: String,
        type: String,
        description: String
    ): Resource<Chat> {
        return when (val result = safeApiCall {
            chatApi.createChat(CreateChatRequestDto(language, name, type, description))
        }) {
            is Resource.Error -> result
            Resource.Loading -> Resource.Loading
            is Resource.Success -> Resource.Success(result.data.toDomain())
        }
    }
}
