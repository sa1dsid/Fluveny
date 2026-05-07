package ru.fluveny.domain.model

data class Dashboard(
    val profile: UserProfile,
    val chats: List<Chat>,
    val messagesCount: Int,
    val recommendations: List<Recommendation>
)
