package com.example.kakaobank.domain.repository

import com.example.kakaobank.domain.model.MediaItem

interface IBookmarkRepository {
    fun getBookmarks(): List<MediaItem>
    fun saveBookmarks(bookmarks: List<MediaItem>)
}
