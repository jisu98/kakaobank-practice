package com.example.kakaobank.domain.usecase

import com.example.kakaobank.domain.model.MediaItem
import com.example.kakaobank.domain.repository.IBookmarkRepository
import javax.inject.Inject

class ToggleBookmarkUseCase @Inject constructor(
    private val bookmarkRepository: IBookmarkRepository,
) {
    operator fun invoke(item: MediaItem): Boolean {
        val bookmarks = bookmarkRepository.getBookmarks().toMutableList()
        val existing = bookmarks.find { it.imageUrl == item.imageUrl }
        return if (existing != null) {
            bookmarks.remove(existing)
            bookmarkRepository.saveBookmarks(bookmarks)
            false
        } else {
            bookmarks.add(item.copy(isBookmarked = true, savedAt = System.currentTimeMillis()))
            bookmarkRepository.saveBookmarks(bookmarks)
            true
        }
    }
}
