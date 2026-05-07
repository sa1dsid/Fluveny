package ru.fluveny.presentation.recommendations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.fluveny.databinding.FragmentRecommendationsBinding
import ru.fluveny.presentation.common.appContainer
import ru.fluveny.presentation.common.collectFlow
import ru.fluveny.presentation.common.showSnackbar

class RecommendationsFragment : Fragment() {
    private var _binding: FragmentRecommendationsBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val viewModel: RecommendationsViewModel by viewModels { appContainer.viewModelFactory }
    private val adapter = RecommendationAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRecommendationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        recommendationsRecycler.adapter = adapter
        regenerateButton.setOnClickListener { viewModel.generate("RU") }
        historyButton.setOnClickListener { root.showSnackbar("История рекомендаций уже загружена ниже") }
        collectFlow(viewModel.uiState) { state ->
            progressBar.isVisible = state.isLoading
            regenerateButton.isEnabled = !state.isLoading && state.canGenerate
            emptyText.isVisible = !state.isLoading && state.recommendations.isEmpty()
            state.error?.let { root.showSnackbar(it) }
            adapter.submitList(state.recommendations)
        }
        viewModel.load()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
