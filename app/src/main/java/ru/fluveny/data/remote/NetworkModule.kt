package ru.fluveny.data.remote

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.fluveny.data.remote.api.AuthApi
import ru.fluveny.data.remote.api.ChatApi
import ru.fluveny.data.remote.api.MessageApi
import ru.fluveny.data.remote.api.RecommendationApi
import ru.fluveny.data.remote.api.UserApi
import ru.fluveny.data.token.AuthInterceptor
import java.util.concurrent.TimeUnit

class NetworkModule(
    private val baseUrl: String,
    private val authInterceptor: AuthInterceptor
) {
    val gson: Gson = GsonBuilder().create()

    private val okHttpClient: OkHttpClient by lazy {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logger)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val authApi: AuthApi by lazy { retrofit.create(AuthApi::class.java) }
    val chatApi: ChatApi by lazy { retrofit.create(ChatApi::class.java) }
    val messageApi: MessageApi by lazy { retrofit.create(MessageApi::class.java) }
    val recommendationApi: RecommendationApi by lazy { retrofit.create(RecommendationApi::class.java) }
    val userApi: UserApi by lazy { retrofit.create(UserApi::class.java) }
}
