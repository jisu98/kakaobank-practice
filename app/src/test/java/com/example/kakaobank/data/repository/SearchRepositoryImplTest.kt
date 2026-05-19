package com.example.kakaobank.data.repository

import com.example.kakaobank.data.remote.KakaoSearchApi
import com.example.kakaobank.data.remote.dto.ImageDocument
import com.example.kakaobank.data.remote.dto.ImageSearchResponse
import com.example.kakaobank.data.remote.dto.VideoDocument
import com.example.kakaobank.data.remote.dto.VideoSearchResponse
import com.example.kakaobank.domain.model.MediaType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SearchRepositoryImplTest {
    private lateinit var api: KakaoSearchApi
    private lateinit var repository: SearchRepositoryImpl

    @Before
    fun setUp() {
        api = mockk()
        repository = SearchRepositoryImpl(api)
    }

    @Test
    fun `given api returns image documents when searchImages then maps to MediaItem list`() = runTest {
        val doc = imageDocument(thumbnailUrl = "thumb_url", imageUrl = "image_url", datetime = "2024-01-01T00:00:00.000+09:00")
        coEvery { api.searchImages(any(), any(), any(), any()) } returns ImageSearchResponse(listOf(doc))

        val result = repository.searchImages("kakao", 1, 30)

        assertEquals(1, result.size)
        assertEquals("thumb_url", result[0].thumbnailUrl)
        assertEquals("image_url", result[0].imageUrl)
        assertEquals("2024-01-01T00:00:00.000+09:00", result[0].datetime)
        assertEquals(MediaType.IMAGE, result[0].type)
        assertFalse(result[0].isBookmarked)
    }

    @Test
    fun `given api returns empty documents when searchImages then returns empty list`() = runTest {
        coEvery { api.searchImages(any(), any(), any(), any()) } returns ImageSearchResponse(emptyList())

        val result = repository.searchImages("kakao", 1, 30)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `given api returns video documents when searchVideos then maps to MediaItem list`() = runTest {
        val doc = videoDocument(thumbnail = "thumb_url", url = "video_url", datetime = "2024-01-01T00:00:00.000+09:00")
        coEvery { api.searchVideos(any(), any(), any(), any()) } returns VideoSearchResponse(listOf(doc))

        val result = repository.searchVideos("kakao", 1, 30)

        assertEquals(1, result.size)
        assertEquals("thumb_url", result[0].thumbnailUrl)
        assertEquals("video_url", result[0].videoUrl)
        assertEquals("2024-01-01T00:00:00.000+09:00", result[0].datetime)
        assertEquals(MediaType.VIDEO, result[0].type)
        assertFalse(result[0].isBookmarked)
    }

    @Test
    fun `given api returns empty documents when searchVideos then returns empty list`() = runTest {
        coEvery { api.searchVideos(any(), any(), any(), any()) } returns VideoSearchResponse(emptyList())

        val result = repository.searchVideos("kakao", 1, 30)

        assertTrue(result.isEmpty())
    }

    private fun imageDocument(
        thumbnailUrl: String = "thumb",
        imageUrl: String = "image",
        datetime: String = "2024-01-01T00:00:00.000+09:00",
    ) = ImageDocument(
        collection = "news",
        thumbnailUrl = thumbnailUrl,
        imageUrl = imageUrl,
        width = 100,
        height = 100,
        displaySitename = "site",
        docUrl = "doc_url",
        datetime = datetime,
    )

    private fun videoDocument(
        thumbnail: String = "thumb",
        url: String = "video",
        datetime: String = "2024-01-01T00:00:00.000+09:00",
    ) = VideoDocument(
        title = "title",
        url = url,
        datetime = datetime,
        playTime = 120,
        thumbnail = thumbnail,
        author = "author",
    )
}
