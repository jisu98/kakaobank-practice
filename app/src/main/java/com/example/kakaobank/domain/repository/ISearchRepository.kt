package com.example.kakaobank.domain.repository

import com.example.kakaobank.domain.model.MediaItem

interface ISearchRepository {
    suspend fun searchImages(query: String, page: Int, size: Int): List<MediaItem>
    suspend fun searchVideos(query: String, page: Int, size: Int): List<MediaItem>
}
