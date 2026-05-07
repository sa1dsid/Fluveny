package ru.fluveny.presentation.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.fluveny.R
import ru.fluveny.databinding.FragmentCreateChatBinding
import ru.fluveny.domain.model.ChatOptions
import ru.fluveny.presentation.common.appContainer
import ru.fluveny.presentation.common.collectFlow
import ru.fluveny.presentation.common.showSnackbar

class CreateChatFragment : Fragment() {
    private var _binding: FragmentCreateChatBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val viewModel: CreateChatViewModel by viewModels { appContainer.viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCreateChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        languageInput.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, ChatOptions.languages)
        )
        typeInput.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, ChatOptions.types)
        )
        languageInput.setText("RU", false)
        typeInput.setText("DAILY_LIFE", false)
        createButton.setOnClickListener {
            viewModel.create(
                languageInput.text?.toString().orEmpty(),
                nameInput.text?.toString().orEmpty(),
                typeInput.text?.toString().orEmpty(),
                descriptionInput.text?.toString().orEmpty()
            )
        }
        collectFlow(viewModel.uiState) { state ->
            progressBar.isVisible = state.isLoading
            createButton.isEnabled = !state.isLoading
            state.error?.let { root.showSnackbar(it) }
            state.createdChat?.let { chat ->
                val args = Bundle().apply {
                    putLong("chatId", chat.id)
                    putString("chatName", chat.name)
                }
                findNavController().navigate(R.id.action_createChatFragment_to_chatFragment, args)
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
