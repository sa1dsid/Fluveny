package ru.fluveny.domain.usecase

import ru.fluveny.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) = repository.login(email, password)
}

class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(username: String, email: String, password: String, language: String) =
        repository.register(username, email, password, language)
}

class LogoutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() = repository.logout()
}

class ChangePasswordUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(currentPassword: String, newPassword: String) =
        repository.changePassword(currentPassword, newPassword)
}
