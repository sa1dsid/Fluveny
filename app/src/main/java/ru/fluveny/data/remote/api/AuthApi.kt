package ru.fluveny.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.fluveny.data.remote.dto.AuthTokensDto
import ru.fluveny.data.remote.dto.ChangePasswordRequestDto
import ru.fluveny.data.remote.dto.LoginRequestDto
import ru.fluveny.data.remote.dto.LogoutRequestDto
import ru.fluveny.data.remote.dto.RegisterRequestDto

interface AuthApi {
    @POST("api/auth/login")
    suspend fun login(@Body body: LoginRequestDto): Response<AuthTokensDto>

    @POST("api/auth/register")
    suspend fun register(@Body body: RegisterRequestDto): Response<Unit>

    @POST("api/auth/change-password")
    suspend fun changePassword(@Body body: ChangePasswordRequestDto): Response<Unit>

    @POST("api/logout")
    suspend fun logout(@Body body: LogoutRequestDto): Response<Unit>
}
