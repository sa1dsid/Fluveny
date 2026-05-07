package ru.fluveny.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.fluveny.R
import ru.fluveny.databinding.FragmentLoginBinding
import ru.fluveny.presentation.common.appContainer
import ru.fluveny.presentation.common.collectFlow
import ru.fluveny.presentation.common.showSnackbar

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val viewModel: LoginViewModel by viewModels { appContainer.viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        loginButton.setOnClickListener {
            viewModel.login(
                emailInput.text?.toString().orEmpty(),
                passwordInput.text?.toString().orEmpty()
            )
        }
        registerLink.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        collectFlow(viewModel.uiState) { state ->
            progressBar.isVisible = state.isLoading
            loginButton.isEnabled = !state.isLoading
            if (state.error != null) {
                root.showSnackbar(state.error)
                viewModel.clearError()
            }
            if (state.isLoggedIn || state.hasSavedSession) {
                findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
