package ru.fluveny.domain.model

data class UserProfile(
    val id: Long?,
    val username: String,
    val displayName: String?,
    val email: String,
    val language: String?
)
