package com.example.kakaobank.data.repository

import com.example.kakaobank.data.local.BookmarkLocalDataSource
import com.example.kakaobank.domain.model.MediaItem
import com.example.kakaobank.domain.model.MediaType
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class BookmarkRepositoryImplTest {
    private lateinit var localDataSource: BookmarkLocalDataSource
    private lateinit var repository: BookmarkRepositoryImpl

    @Before
    fun setUp() {
        localDataSource = mockk()
        repository = BookmarkRepositoryImpl(localDataSource)
    }

    @Test
    fun `given local data source has bookmarks when getBookmarks then returns bookmarks`() {
        val bookmarks = listOf(mediaItem("thumb1"))
        every { localDataSource.getBookmarks() } returns bookmarks

        val result = repository.getBookmarks()

        assertEquals(bookmarks, result)
    }

    @Test
    fun `given bookmarks when saveBookmarks then delegates to local data source`() {
        val bookmarks = listOf(mediaItem("thumb1"))
        val slot = slot<List<MediaItem>>()
        every { localDataSource.saveBookmarks(capture(slot)) } just runs

        repository.saveBookmarks(bookmarks)

        assertEquals(bookmarks, slot.captured)
    }

    private fun mediaItem(thumbnailUrl: String) = MediaItem(
        thumbnailUrl = thumbnailUrl,
        datetime = "2024-01-01T00:00:00.000+09:00",
        type = MediaType.IMAGE,
        isBookmarked = true,
    )
}
