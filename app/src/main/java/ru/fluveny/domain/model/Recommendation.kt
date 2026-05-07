package ru.fluveny.domain.model

data class Recommendation(
    val id: Long,
    val currentComplexityScore: Int,
    val language: String,
    val createdAt: String?,
    val startDate: String?,
    val endDate: String?,
    val topicsAnalytics: List<TopicAnalytics>
)

data class TopicAnalytics(
    val id: Long,
    val type: String,
    val totalMessages: Int,
    val mistakesCount: Int,
    val complexityScore: Int,
    val description: String?
)
