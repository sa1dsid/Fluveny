package ru.fluveny.presentation.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.fluveny.R
import ru.fluveny.databinding.FragmentDashboardBinding
import ru.fluveny.presentation.chats.ChatAdapter
import ru.fluveny.presentation.common.appContainer
import ru.fluveny.presentation.common.collectFlow
import ru.fluveny.presentation.common.showSnackbar
import ru.fluveny.presentation.recommendations.RecommendationAdapter

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val viewModel: DashboardViewModel by viewModels { appContainer.viewModelFactory }
    private val chatAdapter = ChatAdapter { chat ->
        val args = Bundle().apply {
            putLong("chatId", chat.id)
            putString("chatName", chat.name)
        }
        findNavController().navigate(R.id.action_dashboardFragment_to_chatFragment, args)
    }
    private val recommendationAdapter = RecommendationAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        recentChatsRecycler.adapter = chatAdapter
        recommendationsRecycler.adapter = recommendationAdapter
        collectFlow(viewModel.uiState) { state ->
            progressBar.isVisible = state.isLoading
            state.error?.let { root.showSnackbar(it) }
            state.dashboard?.let { dashboard ->
                greetingText.text = "С возвращением, ${dashboard.profile.username.ifBlank { "друг" }}!"
                chatsCountText.text = dashboard.chats.size.toString()
                recommendationsCountText.text = dashboard.recommendations.size.toString()
                messagesCountText.text = dashboard.messagesCount.toString()
                chatAdapter.submitList(dashboard.chats.take(4))
                recommendationAdapter.submitList(dashboard.recommendations.take(2))
                emptyChatsText.isVisible = dashboard.chats.isEmpty()
                emptyRecommendationsText.isVisible = dashboard.recommendations.isEmpty()
            }
        }
        viewModel.load()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
