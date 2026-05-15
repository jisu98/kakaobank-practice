package com.example.kakaobank.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kakaobank.domain.model.MediaItem
import com.example.kakaobank.domain.usecase.SearchMediaUseCase
import com.example.kakaobank.domain.usecase.ToggleBookmarkUseCase
import com.example.kakaobank.presentation.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMediaUseCase: SearchMediaUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<MediaItem>>>(UiState.Idle)
    val uiState: StateFlow<UiState<List<MediaItem>>> = _uiState

    fun search(query: String) {
        if (query.isBlank()) return
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            runCatching { searchMediaUseCase(query) }
                .onSuccess { _uiState.value = UiState.Success(it) }
                .onFailure { _uiState.value = UiState.Error(it.message ?: "오류가 발생했습니다.") }
        }
    }

    fun toggleBookmark(item: MediaItem) {
        val isNowBookmarked = toggleBookmarkUseCase(item)
        val current = (_uiState.value as? UiState.Success)?.data ?: return
        _uiState.value = UiState.Success(
            current.map {
                if (it.imageUrl == item.imageUrl) it.copy(isBookmarked = isNowBookmarked) else it
            },
        )
    }
}
