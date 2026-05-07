package ru.fluveny.data.remote.dto

data class ChatDto(
    val id: Long?,
    val name: String?,
    val language: String?,
    val type: String?,
    val description: String?,
    val createdAt: String?
)

data class CreateChatRequestDto(
    val language: String,
    val name: String,
    val type: String,
    val description: String
)
