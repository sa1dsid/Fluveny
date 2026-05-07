package ru.fluveny.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.fluveny.domain.model.Dashboard
import ru.fluveny.domain.model.Resource
import ru.fluveny.domain.usecase.GetDashboardUseCase

data class DashboardUiState(
    val isLoading: Boolean = false,
    val dashboard: Dashboard? = null,
    val error: String? = null
)

class DashboardViewModel(private val getDashboardUseCase: GetDashboardUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    fun load() {
        viewModelScope.launch {
            _uiState.value = DashboardUiState(isLoading = true)
            _uiState.value = when (val result = getDashboardUseCase()) {
                is Resource.Error -> DashboardUiState(error = result.message)
                Resource.Loading -> DashboardUiState(isLoading = true)
                is Resource.Success -> DashboardUiState(dashboard = result.data)
            }
        }
    }
}
