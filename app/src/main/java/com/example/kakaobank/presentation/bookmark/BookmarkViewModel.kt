package com.example.kakaobank.presentation.bookmark

import androidx.lifecycle.ViewModel
import com.example.kakaobank.domain.model.MediaItem
import com.example.kakaobank.domain.usecase.GetBookmarksUseCase
import com.example.kakaobank.domain.usecase.ToggleBookmarkUseCase
import com.example.kakaobank.presentation.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val getBookmarksUseCase: GetBookmarksUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<MediaItem>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<MediaItem>>> = _uiState

    fun loadBookmarks() {
        _uiState.value = UiState.Success(getBookmarksUseCase())
    }

    fun removeBookmark(item: MediaItem) {
        toggleBookmarkUseCase(item)
        loadBookmarks()
    }
}
