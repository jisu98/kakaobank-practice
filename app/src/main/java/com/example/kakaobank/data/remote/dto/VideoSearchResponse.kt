package com.example.kakaobank.data.remote.dto

import com.google.gson.annotations.SerializedName

data class VideoSearchResponse(
    @SerializedName("documents") val documents: List<VideoDocument>,
)

data class VideoDocument(
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String,
    @SerializedName("datetime") val datetime: String,
    @SerializedName("play_time") val playTime: Int,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("author") val author: String,
)
