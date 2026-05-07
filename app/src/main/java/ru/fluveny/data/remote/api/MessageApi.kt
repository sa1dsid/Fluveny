package ru.fluveny.data.remote.api

import com.google.gson.JsonElement
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.fluveny.data.remote.dto.CorrectionRequestDto
import ru.fluveny.data.remote.dto.CorrectionResultDto
import ru.fluveny.data.remote.dto.MessageDto
import ru.fluveny.data.remote.dto.SendMessageRequestDto

interface MessageApi {
    @GET("api/messages")
    suspend fun getMessages(
        @Query("chatId") chatId: Long,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<JsonElement>

    @POST("api/messages")
    suspend fun sendMessage(@Body body: SendMessageRequestDto): Response<MessageDto>

    @GET("api/messages/count")
    suspend fun getMessagesCount(): Response<JsonElement>

    @POST("api/correction-results")
    suspend fun checkMessage(@Body body: CorrectionRequestDto): Response<CorrectionResultDto>
}
