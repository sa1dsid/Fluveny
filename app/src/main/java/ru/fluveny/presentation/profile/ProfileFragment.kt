package ru.fluveny.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.fluveny.R
import ru.fluveny.databinding.FragmentProfileBinding
import ru.fluveny.presentation.common.appContainer
import ru.fluveny.presentation.common.collectFlow
import ru.fluveny.presentation.common.showSnackbar

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val viewModel: ProfileViewModel by viewModels { appContainer.viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        changePasswordButton.setOnClickListener {
            viewModel.changePassword(
                currentPasswordInput.text?.toString().orEmpty(),
                newPasswordInput.text?.toString().orEmpty()
            )
        }
        changeEmailButton.setOnClickListener {
            viewModel.changeEmail(newEmailInput.text?.toString().orEmpty())
        }
        logoutButton.setOnClickListener { viewModel.logout() }
        collectFlow(viewModel.uiState) { state ->
            progressBar.isVisible = state.isLoading
            state.error?.let { root.showSnackbar(it) }
            state.message?.let { root.showSnackbar(it) }
            state.profile?.let { profile ->
                avatarText.text = profile.username.firstOrNull()?.uppercaseChar()?.toString() ?: "F"
                displayNameText.text = profile.displayName ?: profile.username
                usernameText.text = "@${profile.username}"
                emailText.text = profile.email
            }
            if (state.isLoggedOut) {
                findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
            }
        }
        viewModel.load()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
