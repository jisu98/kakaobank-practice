package com.example.kakaobank.data.repository

import com.example.kakaobank.data.local.BookmarkLocalDataSource
import com.example.kakaobank.domain.model.MediaItem
import com.example.kakaobank.domain.repository.IBookmarkRepository
import javax.inject.Inject

class BookmarkRepositoryImpl @Inject constructor(
    private val localDataSource: BookmarkLocalDataSource,
) : IBookmarkRepository {
    override fun getBookmarks(): List<MediaItem> = localDataSource.getBookmarks()

    override fun saveBookmarks(bookmarks: List<MediaItem>) =
        localDataSource.saveBookmarks(bookmarks)
}
