package ru.fluveny.data.remote.dto

data class UserProfileDto(
    val id: Long?,
    val username: String?,
    val displayName: String?,
    val email: String?,
    val language: String?
)

data class ChangeEmailRequestDto(
    val email: String
)
