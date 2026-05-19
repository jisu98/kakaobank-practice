package com.example.kakaobank.domain.usecase

import com.example.kakaobank.domain.model.MediaItem
import com.example.kakaobank.domain.model.MediaType
import com.example.kakaobank.domain.repository.IBookmarkRepository
import com.example.kakaobank.domain.repository.ISearchRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SearchMediaUseCaseTest {
    private lateinit var searchRepository: ISearchRepository
    private lateinit var bookmarkRepository: IBookmarkRepository
    private lateinit var useCase: SearchMediaUseCase

    @Before
    fun setUp() {
        searchRepository = mockk()
        bookmarkRepository = mockk()
        useCase = SearchMediaUseCase(searchRepository, bookmarkRepository)
        every { bookmarkRepository.getBookmarks() } returns emptyList()
    }

    @Test
    fun `given image and video results when invoke then sorted by datetime descending`() = runTest {
        val older = mediaItem("img1", "2024-01-01T10:00:00.000+09:00", MediaType.IMAGE)
        val newer = mediaItem("vid1", "2024-01-02T10:00:00.000+09:00", MediaType.VIDEO)
        coEvery { searchRepository.searchImages(any(), any(), any()) } returns listOf(older)
        coEvery { searchRepository.searchVideos(any(), any(), any()) } returns listOf(newer)

        val result = useCase("test")

        assertEquals(newer.thumbnailUrl, result[0].thumbnailUrl)
        assertEquals(older.thumbnailUrl, result[1].thumbnailUrl)
    }

    @Test
    fun `given image api fails when invoke then exception propagates`() = runTest {
        coEvery { searchRepository.searchImages(any(), any(), any()) } throws RuntimeException("image api error")
        coEvery { searchRepository.searchVideos(any(), any(), any()) } returns emptyList()

        val result = runCatching { useCase("test") }

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is RuntimeException)
    }

    @Test
    fun `given video api fails when invoke then exception propagates`() = runTest {
        coEvery { searchRepository.searchImages(any(), any(), any()) } returns emptyList()
        coEvery { searchRepository.searchVideos(any(), any(), any()) } throws RuntimeException("video api error")

        val result = runCatching { useCase("test") }

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is RuntimeException)
    }

    @Test
    fun `given both results are empty when invoke then returns empty list`() = runTest {
        coEvery { searchRepository.searchImages(any(), any(), any()) } returns emptyList()
        coEvery { searchRepository.searchVideos(any(), any(), any()) } returns emptyList()

        val result = useCase("test")

        assertTrue(result.isEmpty())
    }

    private fun mediaItem(
        thumbnailUrl: String = "url",
        datetime: String = "2024-01-01T00:00:00.000+09:00",
        type: MediaType = MediaType.IMAGE,
    ) = MediaItem(
        thumbnailUrl = thumbnailUrl,
        datetime = datetime,
        type = type,
        isBookmarked = false,
    )
}
