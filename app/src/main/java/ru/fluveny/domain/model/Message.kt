package ru.fluveny.domain.model

data class Message(
    val id: Long,
    val authorType: AuthorType,
    val text: String,
    val chatId: Long,
    val sentAt: String?
)

enum class AuthorType {
    USER,
    AI
}
