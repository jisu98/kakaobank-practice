package com.example.kakaobank.presentation.navigation

import android.net.Uri
import com.example.kakaobank.domain.model.MediaItem
import com.google.gson.Gson

sealed class Screen(val route: String) {
    data object Search : Screen("search")
    data object Bookmark : Screen("bookmark")
    data object Detail : Screen("detail/{mediaItem}") {
        const val ARG_MEDIA_ITEM = "mediaItem"
        fun createRoute(item: MediaItem): String = "detail/${Uri.encode(Gson().toJson(item))}"
    }
}
