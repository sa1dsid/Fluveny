package ru.fluveny.domain.usecase

import ru.fluveny.domain.repository.RecommendationRepository

class GetRecommendationsUseCase(private val repository: RecommendationRepository) {
    suspend operator fun invoke() = repository.getRecommendations()
}

class GetRecommendationAvailabilityUseCase(private val repository: RecommendationRepository) {
    suspend operator fun invoke() = repository.getAvailability()
}

class GenerateRecommendationsUseCase(private val repository: RecommendationRepository) {
    suspend operator fun invoke(language: String) = repository.generate(language)
}
