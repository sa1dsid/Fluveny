package ru.fluveny.presentation.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.fluveny.databinding.FragmentChatBinding
import ru.fluveny.presentation.common.appContainer
import ru.fluveny.presentation.common.collectFlow
import ru.fluveny.presentation.common.showSnackbar

class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val viewModel: ChatViewModel by viewModels { appContainer.viewModelFactory }
    private val messageAdapter = MessageAdapter { viewModel.check(it.id) }
    private val chatId by lazy { requireArguments().getLong("chatId") }
    private val chatName by lazy { requireArguments().getString("chatName").orEmpty() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        toolbar.title = chatName.ifBlank { "Чат" }
        toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        messagesRecycler.adapter = messageAdapter
        sendButton.setOnClickListener {
            val text = messageInput.text?.toString().orEmpty()
            viewModel.send(chatId, text)
            messageInput.setText("")
        }
        collectFlow(viewModel.uiState) { state ->
            progressBar.isVisible = state.isLoading
            sendButton.isEnabled = !state.isSending
            state.error?.let { root.showSnackbar(it) }
            messageAdapter.submitList(state.messages) {
                if (state.messages.isNotEmpty()) {
                    messagesRecycler.scrollToPosition(state.messages.lastIndex)
                }
            }
            state.correction?.let { correction ->
                val message = if (correction.isMistake) correction.correctedText else "Ошибок нет"
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Проверка сообщения")
                    .setMessage(message)
                    .setPositiveButton("Понятно", null)
                    .show()
                viewModel.correctionShown()
            }
        }
        viewModel.load(chatId)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
