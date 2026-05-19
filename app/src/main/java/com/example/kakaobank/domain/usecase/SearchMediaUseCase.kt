package com.example.kakaobank.domain.usecase

import com.example.kakaobank.domain.model.MediaItem
import com.example.kakaobank.domain.repository.IBookmarkRepository
import com.example.kakaobank.domain.repository.ISearchRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class SearchMediaUseCase @Inject constructor(
    private val searchRepository: ISearchRepository,
    private val bookmarkRepository: IBookmarkRepository,
) {
    suspend operator fun invoke(
        query: String,
        page: Int = 1,
        size: Int = 30,
    ): List<MediaItem> = coroutineScope {
        val bookmarkedUrls = bookmarkRepository.getBookmarks().map { it.thumbnailUrl }.toSet()

        val imagesDeferred = async { searchRepository.searchImages(query, page, size) }
        val videosDeferred = async { searchRepository.searchVideos(query, page, size) }

        (imagesDeferred.await() + videosDeferred.await())
            .map { it.copy(isBookmarked = it.thumbnailUrl in bookmarkedUrls) }
            .sortedByDescending { it.datetime }
    }
}
