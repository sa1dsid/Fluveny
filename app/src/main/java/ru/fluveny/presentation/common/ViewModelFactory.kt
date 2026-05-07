package ru.fluveny.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.fluveny.di.AppContainer
import ru.fluveny.presentation.auth.LoginViewModel
import ru.fluveny.presentation.auth.RegisterViewModel
import ru.fluveny.presentation.chat.ChatViewModel
import ru.fluveny.presentation.chats.ChatsViewModel
import ru.fluveny.presentation.chats.CreateChatViewModel
import ru.fluveny.presentation.dashboard.DashboardViewModel
import ru.fluveny.presentation.profile.ProfileViewModel
import ru.fluveny.presentation.recommendations.RecommendationsViewModel

class FluvenyViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            LoginViewModel::class.java -> LoginViewModel(container.loginUseCase, container.tokenStorage)
            RegisterViewModel::class.java -> RegisterViewModel(container.registerUseCase)
            DashboardViewModel::class.java -> DashboardViewModel(container.getDashboardUseCase)
            ChatsViewModel::class.java -> ChatsViewModel(container.getChatsUseCase)
            CreateChatViewModel::class.java -> CreateChatViewModel(container.createChatUseCase)
            ChatViewModel::class.java -> ChatViewModel(
                container.getMessagesUseCase,
                container.sendMessageUseCase,
                container.checkMessageUseCase
            )
            RecommendationsViewModel::class.java -> RecommendationsViewModel(
                container.getRecommendationsUseCase,
                container.generateRecommendationsUseCase,
                container.getRecommendationAvailabilityUseCase
            )
            ProfileViewModel::class.java -> ProfileViewModel(
                container.getAccountUseCase,
                container.changePasswordUseCase,
                container.changeEmailUseCase,
                container.logoutUseCase
            )
            else -> error("Unknown ViewModel: ${modelClass.name}")
        } as T
    }
}
