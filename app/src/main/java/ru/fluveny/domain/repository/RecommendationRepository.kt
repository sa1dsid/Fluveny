package ru.fluveny.domain.repository

import ru.fluveny.domain.model.Recommendation
import ru.fluveny.domain.model.Resource

interface RecommendationRepository {
    suspend fun getRecommendations(): Resource<List<Recommendation>>
    suspend fun getAvailability(): Resource<Boolean>
    suspend fun generate(language: String): Resource<List<Recommendation>>
}
