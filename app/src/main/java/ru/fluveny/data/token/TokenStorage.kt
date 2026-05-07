package ru.fluveny.data.token

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.tokenDataStore by preferencesDataStore("fluveny_tokens")

class TokenStorage(private val context: Context) {
    private val accessTokenKey = stringPreferencesKey("access_token")
    private val refreshTokenKey = stringPreferencesKey("refresh_token")

    val accessTokenFlow: Flow<String?> = context.tokenDataStore.data.map { it[accessTokenKey] }
    val isAuthorizedFlow: Flow<Boolean> = accessTokenFlow.map { !it.isNullOrBlank() }

    suspend fun getAccessToken(): String? = accessTokenFlow.first()

    suspend fun getRefreshToken(): String? = context.tokenDataStore.data.map { it[refreshTokenKey] }.first()

    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        context.tokenDataStore.edit {
            it[accessTokenKey] = accessToken
            it[refreshTokenKey] = refreshToken
        }
    }

    suspend fun clear() {
        context.tokenDataStore.edit { it.clear() }
    }
}
