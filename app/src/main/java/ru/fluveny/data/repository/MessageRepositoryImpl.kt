package ru.fluveny.data.repository

import com.google.gson.Gson
import com.google.gson.JsonElement
import ru.fluveny.data.remote.api.MessageApi
import ru.fluveny.data.remote.dto.CorrectionRequestDto
import ru.fluveny.data.remote.dto.MessageDto
import ru.fluveny.data.remote.dto.SendMessageRequestDto
import ru.fluveny.data.remote.mapper.toDomain
import ru.fluveny.data.remote.parseListEnvelope
import ru.fluveny.data.remote.safeApiCall
import ru.fluveny.domain.model.CorrectionResult
import ru.fluveny.domain.model.Message
import ru.fluveny.domain.model.Resource
import ru.fluveny.domain.repository.MessageRepository

class MessageRepositoryImpl(
    private val messageApi: MessageApi,
    private val gson: Gson
) : MessageRepository {
    override suspend fun getMessages(chatId: Long, page: Int, size: Int): Resource<List<Message>> {
        return when (val result = safeApiCall { messageApi.getMessages(chatId, page, size) }) {
            is Resource.Error -> result
            Resource.Loading -> Resource.Loading
            is Resource.Success -> Resource.Success(
                gson.parseListEnvelope<MessageDto>(result.data).map(MessageDto::toDomain)
            )
        }
    }

    override suspend fun sendMessage(chatId: Long, message: String): Resource<Message> {
        return when (val result = safeApiCall {
            messageApi.sendMessage(SendMessageRequestDto(chatId, message))
        }) {
            is Resource.Error -> result
            Resource.Loading -> Resource.Loading
            is Resource.Success -> Resource.Success(result.data.toDomain())
        }
    }

    override suspend fun checkMessage(messageId: Long): Resource<CorrectionResult> {
        return when (val result = safeApiCall {
            messageApi.checkMessage(CorrectionRequestDto(messageId))
        }) {
            is Resource.Error -> result
            Resource.Loading -> Resource.Loading
            is Resource.Success -> Resource.Success(result.data.toDomain())
        }
    }

    override suspend fun getMessagesCount(): Resource<Int> {
        return when (val result = safeApiCall { messageApi.getMessagesCount() }) {
            is Resource.Error -> result
            Resource.Loading -> Resource.Loading
            is Resource.Success -> Resource.Success(parseCount(result.data))
        }
    }

    private fun parseCount(json: JsonElement): Int {
        return when {
            json.isJsonPrimitive -> json.asInt
            json.isJsonObject && json.asJsonObject.has("count") -> json.asJsonObject["count"].asInt
            json.isJsonObject && json.asJsonObject.has("total") -> json.asJsonObject["total"].asInt
            else -> 0
        }
    }
}
