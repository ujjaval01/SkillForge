package com.uv.skillforge.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uv.skillforge.data.model.SkillforgeResponse
import com.uv.skillforge.data.repository.SkillforgeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class SkillforgeUiState {
    object Loading : SkillforgeUiState()
    data class Success(val data: SkillforgeResponse) : SkillforgeUiState()
    data class Error(val message: String) : SkillforgeUiState()
}

class SkillforgeViewModel : ViewModel() {
    private val repository = SkillforgeRepository()

    private val _uiState = MutableStateFlow<SkillforgeUiState>(SkillforgeUiState.Loading)
    val uiState: StateFlow<SkillforgeUiState> = _uiState.asStateFlow()

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            _uiState.value = SkillforgeUiState.Loading
            try {
                val response = repository.getSkillforgeData()
                _uiState.value = SkillforgeUiState.Success(response)
            } catch (e: Exception) {
                _uiState.value = SkillforgeUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}
