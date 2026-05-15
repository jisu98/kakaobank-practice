package com.example.kakaobank.domain.usecase

import com.example.kakaobank.domain.model.MediaItem
import com.example.kakaobank.domain.repository.IBookmarkRepository
import javax.inject.Inject

class GetBookmarksUseCase @Inject constructor(
    private val bookmarkRepository: IBookmarkRepository,
) {
    operator fun invoke(): List<MediaItem> =
        bookmarkRepository.getBookmarks().sortedByDescending { it.savedAt }
}
