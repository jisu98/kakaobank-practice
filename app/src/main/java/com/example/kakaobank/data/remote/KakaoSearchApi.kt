package com.example.kakaobank.data.remote

import com.example.kakaobank.data.remote.dto.ImageSearchResponse
import com.example.kakaobank.data.remote.dto.VideoSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoSearchApi {
    @GET("v2/search/image")
    suspend fun searchImages(
        @Query("query") query: String,
        @Query("sort") sort: String = "recency",
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 30,
    ): ImageSearchResponse

    @GET("v2/search/vclip")
    suspend fun searchVideos(
        @Query("query") query: String,
        @Query("sort") sort: String = "recency",
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 30,
    ): VideoSearchResponse
}
