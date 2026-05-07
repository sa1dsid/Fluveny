package ru.fluveny.data.remote.mapper

import ru.fluveny.data.remote.dto.AuthTokensDto
import ru.fluveny.data.remote.dto.ChatDto
import ru.fluveny.data.remote.dto.CorrectionResultDto
import ru.fluveny.data.remote.dto.MessageDto
import ru.fluveny.data.remote.dto.RecommendationDto
import ru.fluveny.data.remote.dto.TopicAnalyticsDto
import ru.fluveny.data.remote.dto.UserProfileDto
import ru.fluveny.domain.model.AuthTokens
import ru.fluveny.domain.model.AuthorType
import ru.fluveny.domain.model.Chat
import ru.fluveny.domain.model.CorrectionResult
import ru.fluveny.domain.model.Message
import ru.fluveny.domain.model.Recommendation
import ru.fluveny.domain.model.TopicAnalytics
import ru.fluveny.domain.model.UserProfile

fun AuthTokensDto.toDomain(): AuthTokens = AuthTokens(
    accessToken = accessToken.orEmpty(),
    tokenType = tokenType ?: "Bearer",
    expiresIn = expiresIn ?: 0L,
    refreshToken = refreshToken.orEmpty(),
    refreshExpiresIn = refreshExpiresIn ?: 0L
)

fun ChatDto.toDomain(): Chat = Chat(
    id = id ?: 0L,
    name = name.orEmpty(),
    language = language.orEmpty(),
    type = type.orEmpty(),
    description = description,
    createdAt = createdAt
)

fun MessageDto.toDomain(): Message = Message(
    id = id ?: 0L,
    authorType = runCatching { AuthorType.valueOf(authorType.orEmpty()) }.getOrDefault(AuthorType.AI),
    text = text.orEmpty(),
    chatId = chatId ?: 0L,
    sentAt = sentAt
)

fun UserProfileDto.toDomain(): UserProfile = UserProfile(
    id = id,
    username = username.orEmpty(),
    displayName = displayName,
    email = email.orEmpty(),
    language = language
)

fun RecommendationDto.toDomain(): Recommendation = Recommendation(
    id = id ?: 0L,
    currentComplexityScore = currentComplexityScore ?: 0,
    language = language.orEmpty(),
    createdAt = createdAt,
    startDate = startDate,
    endDate = endDate,
    topicsAnalytics = topicsAnalytics.orEmpty().map(TopicAnalyticsDto::toDomain)
)

fun TopicAnalyticsDto.toDomain(): TopicAnalytics = TopicAnalytics(
    id = id ?: 0L,
    type = type.orEmpty(),
    totalMessages = totalMessages ?: 0,
    mistakesCount = mistakesCount ?: 0,
    complexityScore = complexityScore ?: 0,
    description = description
)

fun CorrectionResultDto.toDomain(): CorrectionResult = CorrectionResult(
    id = id ?: 0L,
    correctedText = correctedText.orEmpty(),
    isMistake = isMistake ?: false
)
