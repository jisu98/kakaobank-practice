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

    private val _isPaginating = MutableStateFlow(false)
    val isPaginating: StateFlow<Boolean> = _isPaginating

    private var currentPage = 1
    private var lastQuery = ""
    private var isLastPage = false

    fun search(query: String) {
        if (query.isBlank()) return

        lastQuery = query
        currentPage = 1
        isLastPage = false
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            runCatching { searchMediaUseCase(query, page = 1) }
                .onSuccess { results ->
                    isLastPage = results.isEmpty()
                    _uiState.value = UiState.Success(results)
                }
                .onFailure { _uiState.value = UiState.Error(it.message ?: "오류가 발생했습니다.") }
        }
    }

    fun loadMore() {
        if (_isPaginating.value || isLastPage || lastQuery.isBlank()) return

        val current = (_uiState.value as? UiState.Success)?.data ?: return

        _isPaginating.value = true

        viewModelScope.launch {
            runCatching { searchMediaUseCase(lastQuery, page = currentPage + 1) }
                .onSuccess { results ->
                    currentPage++
                    isLastPage = results.isEmpty()
                    _uiState.value = UiState.Success(current + results)
                }
                .onFailure { }

            _isPaginating.value = false
        }
    }

    fun toggleBookmark(item: MediaItem) {
        val isNowBookmarked = toggleBookmarkUseCase(item)
        val current = (_uiState.value as? UiState.Success)?.data ?: return

        _uiState.value = UiState.Success(
            current.map {
                if (it.imageUrl == item.imageUrl) {
                    it.copy(isBookmarked = isNowBookmarked)
                } else {
                    it
                }
            },
        )
    }
}
