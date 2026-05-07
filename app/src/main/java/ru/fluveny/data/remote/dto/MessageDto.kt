package ru.fluveny.data.remote.dto

data class MessageDto(
    val id: Long?,
    val authorType: String?,
    val text: String?,
    val chatId: Long?,
    val sentAt: String?
)

data class SendMessageRequestDto(
    val chatId: Long,
    val message: String
)

data class MessageCountDto(
    val count: Int?
)
