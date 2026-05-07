package ru.fluveny.domain.model

data class AuthTokens(
    val accessToken: String,
    val tokenType: String,
    val expiresIn: Long,
    val refreshToken: String,
    val refreshExpiresIn: Long
)
