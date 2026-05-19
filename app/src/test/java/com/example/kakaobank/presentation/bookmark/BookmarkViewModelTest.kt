package com.example.kakaobank.presentation.bookmark

import com.example.kakaobank.domain.model.MediaItem
import com.example.kakaobank.domain.model.MediaType
import com.example.kakaobank.domain.usecase.GetBookmarksUseCase
import com.example.kakaobank.domain.usecase.ToggleBookmarkUseCase
import com.example.kakaobank.presentation.UiState
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class BookmarkViewModelTest {
    private lateinit var getBookmarksUseCase: GetBookmarksUseCase
    private lateinit var toggleBookmarkUseCase: ToggleBookmarkUseCase
    private lateinit var viewModel: BookmarkViewModel

    @Before
    fun setUp() {
        getBookmarksUseCase = mockk()
        toggleBookmarkUseCase = mockk()
        viewModel = BookmarkViewModel(getBookmarksUseCase, toggleBookmarkUseCase)
    }

    @Test
    fun `given bookmarks when loadBookmarks then uiState is Success with bookmarks`() {
        val bookmarks = listOf(mediaItem("thumb1"), mediaItem("thumb2"))
        every { getBookmarksUseCase() } returns bookmarks

        viewModel.loadBookmarks()

        val state = viewModel.uiState.value as UiState.Success
        assertEquals(bookmarks, state.data)
    }

    @Test
    fun `given no bookmarks when loadBookmarks then uiState is Success with empty list`() {
        every { getBookmarksUseCase() } returns emptyList()

        viewModel.loadBookmarks()

        val state = viewModel.uiState.value as UiState.Success
        assertTrue(state.data.isEmpty())
    }

    @Test
    fun `given item exists when removeBookmark then bookmark is removed and list refreshed`() {
        val item = mediaItem("thumb1")
        every { toggleBookmarkUseCase(any()) } returns false
        every { getBookmarksUseCase() } returnsMany listOf(listOf(item), emptyList())

        viewModel.loadBookmarks()
        viewModel.removeBookmark(item)

        val state = viewModel.uiState.value as UiState.Success
        assertTrue(state.data.isEmpty())
    }

    private fun mediaItem(thumbnailUrl: String = "url") = MediaItem(
        thumbnailUrl = thumbnailUrl,
        datetime = "2024-01-01T00:00:00.000+09:00",
        type = MediaType.IMAGE,
        isBookmarked = true,
    )
}
