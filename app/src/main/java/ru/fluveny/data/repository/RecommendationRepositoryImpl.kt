package ru.fluveny.data.repository

import com.google.gson.Gson
import com.google.gson.JsonElement
import ru.fluveny.data.remote.api.RecommendationApi
import ru.fluveny.data.remote.dto.RecommendationDto
import ru.fluveny.data.remote.mapper.toDomain
import ru.fluveny.data.remote.parseListEnvelope
import ru.fluveny.data.remote.safeApiCall
import ru.fluveny.domain.model.Recommendation
import ru.fluveny.domain.model.Resource
import ru.fluveny.domain.repository.RecommendationRepository

class RecommendationRepositoryImpl(
    private val recommendationApi: RecommendationApi,
    private val gson: Gson
) : RecommendationRepository {
    override suspend fun getRecommendations(): Resource<List<Recommendation>> {
        return parseRecommendationList { recommendationApi.getRecommendations() }
    }

    override suspend fun getAvailability(): Resource<Boolean> {
        return when (val result = safeApiCall { recommendationApi.getAvailability() }) {
            is Resource.Error -> result
            Resource.Loading -> Resource.Loading
            is Resource.Success -> Resource.Success(parseAvailability(result.data))
        }
    }

    override suspend fun generate(language: String): Resource<List<Recommendation>> {
        return parseRecommendationList { recommendationApi.generate(language) }
    }

    private suspend fun parseRecommendationList(
        call: suspend () -> retrofit2.Response<JsonElement>
    ): Resource<List<Recommendation>> {
        return when (val result = safeApiCall(call)) {
            is Resource.Error -> result
            Resource.Loading -> Resource.Loading
            is Resource.Success -> Resource.Success(
                gson.parseListEnvelope<RecommendationDto>(result.data).map(RecommendationDto::toDomain)
            )
        }
    }

    private fun parseAvailability(json: JsonElement): Boolean {
        return when {
            json.isJsonPrimitive -> json.asBoolean
            json.isJsonObject && json.asJsonObject.has("available") -> json.asJsonObject["available"].asBoolean
            json.isJsonObject && json.asJsonObject.has("isAvailable") -> json.asJsonObject["isAvailable"].asBoolean
            else -> true
        }
    }
}
