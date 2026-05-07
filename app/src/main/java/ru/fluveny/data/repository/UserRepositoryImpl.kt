package ru.fluveny.data.repository

import ru.fluveny.data.remote.api.UserApi
import ru.fluveny.data.remote.dto.ChangeEmailRequestDto
import ru.fluveny.data.remote.mapper.toDomain
import ru.fluveny.data.remote.parseApiError
import ru.fluveny.data.remote.safeApiCall
import ru.fluveny.domain.model.Resource
import ru.fluveny.domain.model.UserProfile
import ru.fluveny.domain.repository.UserRepository

class UserRepositoryImpl(private val userApi: UserApi) : UserRepository {
    override suspend fun getAccount(): Resource<UserProfile> {
        return when (val result = safeApiCall { userApi.getAccount() }) {
            is Resource.Error -> result
            Resource.Loading -> Resource.Loading
            is Resource.Success -> Resource.Success(result.data.toDomain())
        }
    }

    override suspend fun changeEmail(email: String): Resource<Unit> {
        val response = userApi.changeEmail(ChangeEmailRequestDto(email))
        return if (response.isSuccessful) Resource.Success(Unit) else Resource.Error(parseApiError(response))
    }
}
