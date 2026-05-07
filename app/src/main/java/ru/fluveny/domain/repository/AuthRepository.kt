package ru.fluveny.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.fluveny.domain.model.AuthTokens
import ru.fluveny.domain.model.Resource

interface AuthRepository {
    val isAuthorized: Flow<Boolean>
    suspend fun login(email: String, password: String): Resource<AuthTokens>
    suspend fun register(username: String, email: String, password: String, language: String): Resource<Unit>
    suspend fun logout(): Resource<Unit>
    suspend fun changePassword(currentPassword: String, newPassword: String): Resource<Unit>
}
