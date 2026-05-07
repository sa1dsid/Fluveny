package ru.fluveny.presentation.recommendations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.fluveny.domain.model.Recommendation
import ru.fluveny.domain.model.Resource
import ru.fluveny.domain.usecase.GenerateRecommendationsUseCase
import ru.fluveny.domain.usecase.GetRecommendationAvailabilityUseCase
import ru.fluveny.domain.usecase.GetRecommendationsUseCase

data class RecommendationsUiState(
    val isLoading: Boolean = false,
    val recommendations: List<Recommendation> = emptyList(),
    val canGenerate: Boolean = true,
    val error: String? = null
)

class RecommendationsViewModel(
    private val getRecommendationsUseCase: GetRecommendationsUseCase,
    private val generateRecommendationsUseCase: GenerateRecommendationsUseCase,
    private val getRecommendationAvailabilityUseCase: GetRecommendationAvailabilityUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(RecommendationsUiState())
    val uiState: StateFlow<RecommendationsUiState> = _uiState.asStateFlow()

    fun load() {
        viewModelScope.launch {
            _uiState.value = RecommendationsUiState(isLoading = true)
            val availability = getRecommendationAvailabilityUseCase()
            val canGenerate = (availability as? Resource.Success)?.data ?: true
            _uiState.value = when (val result = getRecommendationsUseCase()) {
                is Resource.Error -> RecommendationsUiState(canGenerate = canGenerate, error = result.message)
                Resource.Loading -> RecommendationsUiState(isLoading = true)
                is Resource.Success -> RecommendationsUiState(
                    recommendations = result.data,
                    canGenerate = canGenerate
                )
            }
        }
    }

    fun generate(language: String = "RU") {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            _uiState.value = when (val result = generateRecommendationsUseCase(language)) {
                is Resource.Error -> _uiState.value.copy(isLoading = false, error = result.message)
                Resource.Loading -> _uiState.value.copy(isLoading = true)
                is Resource.Success -> _uiState.value.copy(isLoading = false, recommendations = result.data)
            }
        }
    }
}
