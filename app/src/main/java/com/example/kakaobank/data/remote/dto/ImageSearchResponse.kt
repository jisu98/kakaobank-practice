package com.example.kakaobank.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ImageSearchResponse(
    @SerializedName("documents") val documents: List<ImageDocument>,
)

data class ImageDocument(
    @SerializedName("thumbnail_url") val thumbnailUrl: String,
    @SerializedName("datetime") val datetime: String,
)
