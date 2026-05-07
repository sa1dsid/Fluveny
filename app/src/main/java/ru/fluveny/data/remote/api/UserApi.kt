package ru.fluveny.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.fluveny.data.remote.dto.ChangeEmailRequestDto
import ru.fluveny.data.remote.dto.UserProfileDto

interface UserApi {
    @GET("api/user-profiles/account")
    suspend fun getAccount(): Response<UserProfileDto>

    @POST("api/user-profiles/account/email")
    suspend fun changeEmail(@Body body: ChangeEmailRequestDto): Response<Unit>
}
