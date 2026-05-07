package ru.fluveny.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.fluveny.databinding.FragmentRegisterBinding
import ru.fluveny.domain.model.ChatOptions
import ru.fluveny.presentation.common.appContainer
import ru.fluveny.presentation.common.collectFlow
import ru.fluveny.presentation.common.showSnackbar
import ru.fluveny.presentation.common.showToast

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val viewModel: RegisterViewModel by viewModels { appContainer.viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        languageInput.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, ChatOptions.languages)
        )
        languageInput.setText("RU", false)
        registerButton.setOnClickListener {
            viewModel.register(
                usernameInput.text?.toString().orEmpty(),
                emailInput.text?.toString().orEmpty(),
                passwordInput.text?.toString().orEmpty(),
                languageInput.text?.toString().orEmpty().ifBlank { "RU" }
            )
        }
        loginLink.setOnClickListener { findNavController().popBackStack() }
        collectFlow(viewModel.uiState) { state ->
            progressBar.isVisible = state.isLoading
            registerButton.isEnabled = !state.isLoading
            state.error?.let { root.showSnackbar(it) }
            if (state.isRegistered) {
                showToast("Аккаунт создан. Теперь войдите.")
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
