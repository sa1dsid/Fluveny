package ru.fluveny.data.remote.api

import com.google.gson.JsonElement
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RecommendationApi {
    @GET("api/recommendations")
    suspend fun getRecommendations(): Response<JsonElement>

    @GET("api/recommendations/availability")
    suspend fun getAvailability(): Response<JsonElement>

    @POST("api/recommendations/generate")
    suspend fun generate(@Query("language") language: String): Response<JsonElement>
}
