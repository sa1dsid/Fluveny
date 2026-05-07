package ru.fluveny.domain.model

data class CorrectionResult(
    val id: Long,
    val correctedText: String,
    val isMistake: Boolean
)
