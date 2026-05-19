package com.example.kakaobank.data.local

import android.content.SharedPreferences
import com.example.kakaobank.domain.model.MediaItem
import com.example.kakaobank.domain.model.MediaType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class BookmarkLocalDataSourceTest {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private val gson = Gson()
    private lateinit var dataSource: BookmarkLocalDataSource

    @Before
    fun setUp() {
        sharedPreferences = mockk()
        editor = mockk()
        every { sharedPreferences.edit() } returns editor
        every { editor.putString(any(), any()) } returns editor
        every { editor.apply() } just runs
        dataSource = BookmarkLocalDataSource(sharedPreferences, gson)
    }

    @Test
    fun `given no saved data when getBookmarks then returns empty list`() {
        every { sharedPreferences.getString("bookmarks", null) } returns null

        val result = dataSource.getBookmarks()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `given saved bookmarks when getBookmarks then returns deserialized list`() {
        val bookmarks = listOf(mediaItem("thumb1"))
        every { sharedPreferences.getString("bookmarks", null) } returns gson.toJson(bookmarks)

        val result = dataSource.getBookmarks()

        assertEquals(1, result.size)
        assertEquals("thumb1", result[0].thumbnailUrl)
    }

    @Test
    fun `given bookmarks when saveBookmarks then serializes and persists to SharedPreferences`() {
        val bookmarks = listOf(mediaItem("thumb1"))
        val jsonSlot = slot<String>()
        every { editor.putString(eq("bookmarks"), capture(jsonSlot)) } returns editor

        dataSource.saveBookmarks(bookmarks)

        val type = object : TypeToken<List<MediaItem>>() {}.type
        val saved: List<MediaItem> = gson.fromJson(jsonSlot.captured, type)
        assertEquals(1, saved.size)
        assertEquals("thumb1", saved[0].thumbnailUrl)
    }

    private fun mediaItem(thumbnailUrl: String) = MediaItem(
        thumbnailUrl = thumbnailUrl,
        datetime = "2024-01-01T00:00:00.000+09:00",
        type = MediaType.IMAGE,
        isBookmarked = true,
    )
}
