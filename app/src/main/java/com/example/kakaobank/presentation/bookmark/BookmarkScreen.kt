package com.example.kakaobank.presentation.bookmark

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kakaobank.presentation.UiState
import com.example.kakaobank.presentation.component.MediaGrid

@Composable
fun BookmarkScreen(viewModel: BookmarkViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LifecycleResumeEffect(Unit) {
        viewModel.loadBookmarks()
        onPauseOrDispose { }
    }
    when (val state = uiState) {
        is UiState.Success -> {
            if (state.data.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(text = "보관된 항목이 없습니다", modifier = Modifier.align(Alignment.Center))
                }
            } else {
                MediaGrid(
                    items = state.data,
                    onBookmarkClick = viewModel::removeBookmark,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
        else -> Unit
    }
}
