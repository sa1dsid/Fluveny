package ru.fluveny.domain.usecase

import ru.fluveny.domain.model.Dashboard
import ru.fluveny.domain.model.Resource
import ru.fluveny.domain.repository.ChatRepository
import ru.fluveny.domain.repository.MessageRepository
import ru.fluveny.domain.repository.RecommendationRepository
import ru.fluveny.domain.repository.UserRepository

class GetAccountUseCase(private val repository: UserRepository) {
    suspend operator fun invoke() = repository.getAccount()
}

class ChangeEmailUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(email: String) = repository.changeEmail(email)
}

class GetDashboardUseCase(
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository,
    private val messageRepository: MessageRepository,
    private val recommendationRepository: RecommendationRepository
) {
    suspend operator fun invoke(): Resource<Dashboard> {
        val profile = userRepository.getAccount()
        if (profile is Resource.Error) return profile
        val chats = chatRepository.getChats()
        if (chats is Resource.Error) return chats
        val messagesCount = messageRepository.getMessagesCount()
        if (messagesCount is Resource.Error) return messagesCount
        val recommendations = recommendationRepository.getRecommendations()
        if (recommendations is Resource.Error) return recommendations

        return Resource.Success(
            Dashboard(
                profile = (profile as Resource.Success).data,
                chats = (chats as Resource.Success).data,
                messagesCount = (messagesCount as Resource.Success).data,
                recommendations = (recommendations as Resource.Success).data
            )
        )
    }
}
