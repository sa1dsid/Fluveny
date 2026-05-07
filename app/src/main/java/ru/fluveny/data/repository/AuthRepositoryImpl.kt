package ru.fluveny.data.repository

import kotlinx.coroutines.flow.Flow
import ru.fluveny.data.remote.api.AuthApi
import ru.fluveny.data.remote.dto.ChangePasswordRequestDto
import ru.fluveny.data.remote.dto.LoginRequestDto
import ru.fluveny.data.remote.dto.LogoutRequestDto
import ru.fluveny.data.remote.dto.RegisterRequestDto
import ru.fluveny.data.remote.mapper.toDomain
import ru.fluveny.data.remote.parseApiError
import ru.fluveny.data.remote.safeApiCall
import ru.fluveny.data.token.TokenStorage
import ru.fluveny.domain.model.AuthTokens
import ru.fluveny.domain.model.Resource
import ru.fluveny.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val tokenStorage: TokenStorage
) : AuthRepository {
    override val isAuthorized: Flow<Boolean> = tokenStorage.isAuthorizedFlow

    override suspend fun login(email: String, password: String): Resource<AuthTokens> {
        return when (val result = safeApiCall { authApi.login(LoginRequestDto(email, password)) }) {
            is Resource.Error -> result
            Resource.Loading -> Resource.Loading
            is Resource.Success -> {
                val tokens = result.data.toDomain()
                if (tokens.accessToken.isBlank()) {
                    Resource.Error("Сервер не вернул access_token")
                } else {
                    tokenStorage.saveTokens(tokens.accessToken, tokens.refreshToken)
                    Resource.Success(tokens)
                }
            }
        }
    }

    override suspend fun register(
        username: String,
        email: String,
        password: String,
        language: String
    ): Resource<Unit> {
        val response = authApi.register(RegisterRequestDto(username, email, password, language))
        return if (response.isSuccessful) Resource.Success(Unit) else Resource.Error(parseApiError(response))
    }

    override suspend fun logout(): Resource<Unit> {
        val refreshToken = tokenStorage.getRefreshToken().orEmpty()
        val response = authApi.logout(LogoutRequestDto(refreshToken))
        tokenStorage.clear()
        return if (response.isSuccessful) Resource.Success(Unit) else Resource.Error(parseApiError(response))
    }

    override suspend fun changePassword(currentPassword: String, newPassword: String): Resource<Unit> {
        val response = authApi.changePassword(ChangePasswordRequestDto(currentPassword, newPassword))
        return if (response.isSuccessful) Resource.Success(Unit) else Resource.Error(parseApiError(response))
    }
}
