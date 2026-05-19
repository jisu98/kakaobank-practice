package com.example.kakaobank.domain.model

data class MediaItem(
    val thumbnailUrl: String,
    val imageUrl: String = "",
    val videoUrl: String = "",
    val datetime: String,
    val type: MediaType,
    val isBookmarked: Boolean,
    val savedAt: Long = 0L,
)

enum class MediaType {
    IMAGE,
    VIDEO,
}
