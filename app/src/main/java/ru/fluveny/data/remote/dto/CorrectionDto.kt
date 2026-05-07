package ru.fluveny.data.remote.dto

data class CorrectionRequestDto(
    val messageId: Long
)

data class CorrectionResultDto(
    val id: Long?,
    val correctedText: String?,
    val isMistake: Boolean?
)
