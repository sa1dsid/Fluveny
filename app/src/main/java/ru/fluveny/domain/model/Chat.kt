package ru.fluveny.domain.model

data class Chat(
    val id: Long,
    val name: String,
    val language: String,
    val type: String,
    val description: String?,
    val createdAt: String?
)
