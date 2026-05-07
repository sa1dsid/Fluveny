package ru.fluveny.presentation.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.fluveny.R
import ru.fluveny.databinding.FragmentChatsBinding
import ru.fluveny.presentation.common.appContainer
import ru.fluveny.presentation.common.collectFlow
import ru.fluveny.presentation.common.showSnackbar

class ChatsFragment : Fragment() {
    private var _binding: FragmentChatsBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val viewModel: ChatsViewModel by viewModels { appContainer.viewModelFactory }
    private val chatAdapter = ChatAdapter { chat ->
        val args = Bundle().apply {
            putLong("chatId", chat.id)
            putString("chatName", chat.name)
        }
        findNavController().navigate(R.id.action_chatsFragment_to_chatFragment, args)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        chatsRecycler.adapter = chatAdapter
        newChatButton.setOnClickListener {
            findNavController().navigate(R.id.action_chatsFragment_to_createChatFragment)
        }
        collectFlow(viewModel.uiState) { state ->
            progressBar.isVisible = state.isLoading
            emptyText.isVisible = !state.isLoading && state.chats.isEmpty()
            state.error?.let { root.showSnackbar(it) }
            chatAdapter.submitList(state.chats)
        }
        viewModel.load()
    }

    override fun onResume() {
        super.onResume()
        viewModel.load()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
