package com.example.kakaobank.presentation.navigation

import android.net.Uri

sealed class Screen(val route: String) {
    data object Search : Screen("search")
    data object Bookmark : Screen("bookmark")
    data object Detail : Screen("detail/{imageUrl}") {
        const val ARG_IMAGE_URL = "imageUrl"
        fun createRoute(imageUrl: String): String = "detail/${Uri.encode(imageUrl)}"
    }
}
