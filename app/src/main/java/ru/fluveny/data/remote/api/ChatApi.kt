package ru.fluveny.data.remote.api

import com.google.gson.JsonElement
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.fluveny.data.remote.dto.ChatDto
import ru.fluveny.data.remote.dto.CreateChatRequestDto

interface ChatApi {
    @GET("api/chats")
    suspend fun getChats(): Response<JsonElement>

    @POST("api/chats")
    suspend fun createChat(@Body body: CreateChatRequestDto): Response<ChatDto>
}
