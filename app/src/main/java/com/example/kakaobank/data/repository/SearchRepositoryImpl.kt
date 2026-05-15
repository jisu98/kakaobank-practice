package com.example.kakaobank.data.repository

import com.example.kakaobank.data.remote.KakaoSearchApi
import com.example.kakaobank.domain.model.MediaItem
import com.example.kakaobank.domain.model.MediaType
import com.example.kakaobank.domain.repository.ISearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val api: KakaoSearchApi,
) : ISearchRepository {

    override suspend fun searchImages(query: String, page: Int, size: Int): List<MediaItem> =
        api.searchImages(query = query, page = page, size = size).documents.map { doc ->
            MediaItem(
                imageUrl = doc.thumbnailUrl,
                datetime = doc.datetime,
                type = MediaType.IMAGE,
                isBookmarked = false,
            )
        }

    override suspend fun searchVideos(query: String, page: Int, size: Int): List<MediaItem> =
        api.searchVideos(query = query, page = page, size = size).documents.map { doc ->
            MediaItem(
                imageUrl = doc.thumbnail,
                datetime = doc.datetime,
                type = MediaType.VIDEO,
                isBookmarked = false,
            )
        }
}
