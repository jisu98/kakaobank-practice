package com.example.kakaobank.data.local

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.kakaobank.domain.model.MediaItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookmarkLocalDataSource @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
) {
    fun getBookmarks(): List<MediaItem> {
        val json = sharedPreferences.getString(KEY_BOOKMARKS, null) ?: return emptyList()
        val type = object : TypeToken<List<MediaItem>>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveBookmarks(bookmarks: List<MediaItem>) {
        sharedPreferences.edit {
            putString(KEY_BOOKMARKS, gson.toJson(bookmarks))
        }
    }

    companion object {
        private const val KEY_BOOKMARKS = "bookmarks"
    }
}
