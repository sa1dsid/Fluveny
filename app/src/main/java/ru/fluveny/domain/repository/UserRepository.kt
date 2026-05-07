package ru.fluveny.domain.repository

import ru.fluveny.domain.model.Resource
import ru.fluveny.domain.model.UserProfile

interface UserRepository {
    suspend fun getAccount(): Resource<UserProfile>
    suspend fun changeEmail(email: String): Resource<Unit>
}
