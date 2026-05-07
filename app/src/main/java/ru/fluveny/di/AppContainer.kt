package ru.fluveny.di

import android.content.Context
import ru.fluveny.R
import ru.fluveny.data.remote.NetworkModule
import ru.fluveny.data.repository.AuthRepositoryImpl
import ru.fluveny.data.repository.ChatRepositoryImpl
import ru.fluveny.data.repository.MessageRepositoryImpl
import ru.fluveny.data.repository.RecommendationRepositoryImpl
import ru.fluveny.data.repository.UserRepositoryImpl
import ru.fluveny.data.token.AuthInterceptor
import ru.fluveny.data.token.TokenStorage
import ru.fluveny.domain.usecase.ChangeEmailUseCase
import ru.fluveny.domain.usecase.ChangePasswordUseCase
import ru.fluveny.domain.usecase.CheckMessageUseCase
import ru.fluveny.domain.usecase.CreateChatUseCase
import ru.fluveny.domain.usecase.GenerateRecommendationsUseCase
import ru.fluveny.domain.usecase.GetAccountUseCase
import ru.fluveny.domain.usecase.GetChatsUseCase
import ru.fluveny.domain.usecase.GetDashboardUseCase
import ru.fluveny.domain.usecase.GetMessagesCountUseCase
import ru.fluveny.domain.usecase.GetMessagesUseCase
import ru.fluveny.domain.usecase.GetRecommendationAvailabilityUseCase
import ru.fluveny.domain.usecase.GetRecommendationsUseCase
import ru.fluveny.domain.usecase.LoginUseCase
import ru.fluveny.domain.usecase.LogoutUseCase
import ru.fluveny.domain.usecase.RegisterUseCase
import ru.fluveny.domain.usecase.SendMessageUseCase
import ru.fluveny.presentation.common.FluvenyViewModelFactory

class AppContainer(context: Context) {
    val tokenStorage = TokenStorage(context.applicationContext)
    private val network = NetworkModule(
        baseUrl = context.getString(R.string.base_url),
        authInterceptor = AuthInterceptor(tokenStorage)
    )

    private val authRepository = AuthRepositoryImpl(network.authApi, tokenStorage)
    private val chatRepository = ChatRepositoryImpl(network.chatApi, network.gson)
    private val messageRepository = MessageRepositoryImpl(network.messageApi, network.gson)
    private val recommendationRepository = RecommendationRepositoryImpl(network.recommendationApi, network.gson)
    private val userRepository = UserRepositoryImpl(network.userApi)

    val loginUseCase = LoginUseCase(authRepository)
    val registerUseCase = RegisterUseCase(authRepository)
    val logoutUseCase = LogoutUseCase(authRepository)
    val changePasswordUseCase = ChangePasswordUseCase(authRepository)
    val getChatsUseCase = GetChatsUseCase(chatRepository)
    val createChatUseCase = CreateChatUseCase(chatRepository)
    val getMessagesUseCase = GetMessagesUseCase(messageRepository)
    val sendMessageUseCase = SendMessageUseCase(messageRepository)
    val checkMessageUseCase = CheckMessageUseCase(messageRepository)
    val getMessagesCountUseCase = GetMessagesCountUseCase(messageRepository)
    val getRecommendationsUseCase = GetRecommendationsUseCase(recommendationRepository)
    val getRecommendationAvailabilityUseCase = GetRecommendationAvailabilityUseCase(recommendationRepository)
    val generateRecommendationsUseCase = GenerateRecommendationsUseCase(recommendationRepository)
    val getAccountUseCase = GetAccountUseCase(userRepository)
    val changeEmailUseCase = ChangeEmailUseCase(userRepository)
    val getDashboardUseCase = GetDashboardUseCase(
        userRepository,
        chatRepository,
        messageRepository,
        recommendationRepository
    )

    val viewModelFactory = FluvenyViewModelFactory(this)
}
