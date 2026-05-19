package com.example.kakaobank.domain.usecase

import com.example.kakaobank.domain.model.MediaItem
import com.example.kakaobank.domain.model.MediaType
import com.example.kakaobank.domain.repository.IBookmarkRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetBookmarksUseCaseTest {
    private lateinit var bookmarkRepository: IBookmarkRepository
    private lateinit var useCase: GetBookmarksUseCase

    @Before
    fun setUp() {
        bookmarkRepository = mockk()
        useCase = GetBookmarksUseCase(bookmarkRepository)
    }

    @Test
    fun `given bookmarks with different savedAt when invoke then sorted by savedAt descending`() {
        val older = mediaItem(thumbnailUrl = "thumb1", savedAt = 1000L)
        val newer = mediaItem(thumbnailUrl = "thumb2", savedAt = 2000L)
        every { bookmarkRepository.getBookmarks() } returns listOf(older, newer)

        val result = useCase()

        assertEquals(newer.thumbnailUrl, result[0].thumbnailUrl)
        assertEquals(older.thumbnailUrl, result[1].thumbnailUrl)
    }

    @Test
    fun `given no bookmarks when invoke then returns empty list`() {
        every { bookmarkRepository.getBookmarks() } returns emptyList()

        val result = useCase()

        assertTrue(result.isEmpty())
    }

    private fun mediaItem(
        thumbnailUrl: String = "url",
        savedAt: Long = 0L,
    ) = MediaItem(
        thumbnailUrl = thumbnailUrl,
        datetime = "2024-01-01T00:00:00.000+09:00",
        type = MediaType.IMAGE,
        isBookmarked = true,
        savedAt = savedAt,
    )
}
