package com.example.kakaobank.domain.usecase

import com.example.kakaobank.domain.model.MediaItem
import com.example.kakaobank.domain.model.MediaType
import com.example.kakaobank.domain.repository.IBookmarkRepository
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ToggleBookmarkUseCaseTest {
    private lateinit var bookmarkRepository: IBookmarkRepository
    private lateinit var useCase: ToggleBookmarkUseCase

    @Before
    fun setUp() {
        bookmarkRepository = mockk()
        useCase = ToggleBookmarkUseCase(bookmarkRepository)
    }

    @Test
    fun `given item not bookmarked when invoke then item is added to bookmarks`() {
        val item = mediaItem()
        val savedSlot = slot<List<MediaItem>>()
        every { bookmarkRepository.getBookmarks() } returns emptyList()
        every { bookmarkRepository.saveBookmarks(capture(savedSlot)) } just runs

        val result = useCase(item)

        assertTrue(result)
        assertTrue(savedSlot.captured.any { it.thumbnailUrl == item.thumbnailUrl })
    }

    @Test
    fun `given item already bookmarked when invoke then item is removed from bookmarks`() {
        val item = mediaItem(isBookmarked = true)
        val savedSlot = slot<List<MediaItem>>()
        every { bookmarkRepository.getBookmarks() } returns listOf(item)
        every { bookmarkRepository.saveBookmarks(capture(savedSlot)) } just runs

        val result = useCase(item)

        assertFalse(result)
        assertTrue(savedSlot.captured.none { it.thumbnailUrl == item.thumbnailUrl })
    }

    private fun mediaItem(isBookmarked: Boolean = false) = MediaItem(
        thumbnailUrl = "thumb_url",
        datetime = "2024-01-01T00:00:00.000+09:00",
        type = MediaType.IMAGE,
        isBookmarked = isBookmarked,
    )
}
