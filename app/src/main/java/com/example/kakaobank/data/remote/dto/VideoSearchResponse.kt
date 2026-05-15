package com.example.kakaobank.data.remote.dto

import com.google.gson.annotations.SerializedName

data class VideoSearchResponse(
    @SerializedName("documents") val documents: List<VideoDocument>,
)

data class VideoDocument(
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("datetime") val datetime: String,
)
