package com.example.kakaobank.presentation.search

import com.example.kakaobank.domain.model.MediaItem
import com.example.kakaobank.domain.model.MediaType
import com.example.kakaobank.domain.usecase.SearchMediaUseCase
import com.example.kakaobank.domain.usecase.ToggleBookmarkUseCase
import com.example.kakaobank.presentation.UiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var searchMediaUseCase: SearchMediaUseCase
    private lateinit var toggleBookmarkUseCase: ToggleBookmarkUseCase
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        searchMediaUseCase = mockk()
        toggleBookmarkUseCase = mockk()
        viewModel = SearchViewModel(searchMediaUseCase, toggleBookmarkUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given blank query when search then uiState stays Idle`() {
        viewModel.search("   ")

        assertTrue(viewModel.uiState.value is UiState.Idle)
    }

    @Test
    fun `given valid query when search succeeds then uiState is Success`() {
        val items = listOf(mediaItem("thumb1"))
        coEvery { searchMediaUseCase(any(), any(), any()) } returns items

        viewModel.search("kakao")

        val state = viewModel.uiState.value as UiState.Success
        assertEquals(items, state.data)
    }

    @Test
    fun `given valid query when search fails then uiState is Error`() {
        coEvery { searchMediaUseCase(any(), any(), any()) } throws RuntimeException("network error")

        viewModel.search("kakao")

        assertTrue(viewModel.uiState.value is UiState.Error)
    }

    @Test
    fun `given success state when loadMore succeeds then results are appended`() {
        val firstPage = listOf(mediaItem("thumb1"))
        val secondPage = listOf(mediaItem("thumb2"))
        coEvery { searchMediaUseCase("kakao", 1, any()) } returns firstPage
        coEvery { searchMediaUseCase("kakao", 2, any()) } returns secondPage

        viewModel.search("kakao")
        viewModel.loadMore()

        val state = viewModel.uiState.value as UiState.Success
        assertEquals(firstPage + secondPage, state.data)
    }

    @Test
    fun `given empty first page result when loadMore then useCase is not called again`() {
        coEvery { searchMediaUseCase(any(), any(), any()) } returns emptyList()

        viewModel.search("kakao")
        viewModel.loadMore()

        coVerify(exactly = 1) { searchMediaUseCase(any(), any(), any()) }
    }

    @Test
    fun `given loadMore returns duplicate thumbnailUrl when loadMore then item is deduplicated`() {
        val item = mediaItem("thumb1", isBookmarked = false)
        val duplicate = mediaItem("thumb1", isBookmarked = true)
        coEvery { searchMediaUseCase("kakao", 1, any()) } returns listOf(item)
        coEvery { searchMediaUseCase("kakao", 2, any()) } returns listOf(duplicate)

        viewModel.search("kakao")
        viewModel.loadMore()

        val state = viewModel.uiState.value as UiState.Success
        assertEquals(1, state.data.size)
        assertTrue(state.data.first().isBookmarked)
    }

    @Test
    fun `given bookmark when toggleBookmark then item isBookmarked is updated`() {
        val item = mediaItem("thumb1", isBookmarked = false)
        coEvery { searchMediaUseCase(any(), any(), any()) } returns listOf(item)
        every { toggleBookmarkUseCase(any()) } returns true

        viewModel.search("kakao")
        viewModel.toggleBookmark(item)

        val state = viewModel.uiState.value as UiState.Success
        assertTrue(state.data.first().isBookmarked)
    }

    @Test
    fun `given bookmark when toggleBookmark then other items are not update`() {
        val item1 = mediaItem("thumb1", isBookmarked = false)
        val item2 = mediaItem("thumb2", isBookmarked = false)
        coEvery { searchMediaUseCase(any(), any(), any()) } returns listOf(item1, item2)
        every { toggleBookmarkUseCase(any()) } returns true

        viewModel.search("kakao")
        viewModel.toggleBookmark(item1)

        val state = viewModel.uiState.value as UiState.Success
        assertFalse(state.data.last().isBookmarked)
    }

    private fun mediaItem(
        thumbnailUrl: String = "url",
        isBookmarked: Boolean = false,
    ) = MediaItem(
        thumbnailUrl = thumbnailUrl,
        datetime = "2024-01-01T00:00:00.000+09:00",
        type = MediaType.IMAGE,
        isBookmarked = isBookmarked,
    )
}
