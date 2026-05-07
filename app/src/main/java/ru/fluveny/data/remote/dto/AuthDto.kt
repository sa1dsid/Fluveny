package ru.fluveny.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginRequestDto(
    val email: String,
    val password: String
)

data class RegisterRequestDto(
    val username: String,
    val email: String,
    val password: String,
    val language: String
)

data class AuthTokensDto(
    @SerializedName("access_token") val accessToken: String?,
    @SerializedName("token_type") val tokenType: String?,
    @SerializedName("expires_in") val expiresIn: Long?,
    @SerializedName("refresh_token") val refreshToken: String?,
    @SerializedName("refresh_expires_in") val refreshExpiresIn: Long?
)

data class ChangePasswordRequestDto(
    val currentPassword: String,
    val newPassword: String
)

data class LogoutRequestDto(
    val refreshToken: String
)
