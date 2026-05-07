package ru.fluveny.data.remote.dto

data class RecommendationDto(
    val id: Long?,
    val currentComplexityScore: Int?,
    val language: String?,
    val createdAt: String?,
    val startDate: String?,
    val endDate: String?,
    val topicsAnalytics: List<TopicAnalyticsDto>?
)

data class TopicAnalyticsDto(
    val id: Long?,
    val type: String?,
    val totalMessages: Int?,
    val mistakesCount: Int?,
    val complexityScore: Int?,
    val description: String?
)

data class RecommendationAvailabilityDto(
    val available: Boolean?,
    val isAvailable: Boolean?
)
