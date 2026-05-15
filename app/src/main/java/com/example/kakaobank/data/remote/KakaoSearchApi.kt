package com.example.kakaobank.data.remote

import com.example.kakaobank.data.remote.dto.ImageSearchResponse
import com.example.kakaobank.data.remote.dto.VideoSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoSearchApi {
    @GET("v2/search/image")
    suspend fun searchImages(
        @Query("query") query: String,
        @Query("sort") sort: String = DEFAULT_SORT,
        @Query("page") page: Int = DEFAULT_PAGE,
        @Query("size") size: Int = DEFAULT_SIZE,
    ): ImageSearchResponse

    @GET("v2/search/vclip")
    suspend fun searchVideos(
        @Query("query") query: String,
        @Query("sort") sort: String = DEFAULT_SORT,
        @Query("page") page: Int = DEFAULT_PAGE,
        @Query("size") size: Int = DEFAULT_SIZE,
    ): VideoSearchResponse

    companion object {
        const val DEFAULT_SORT = "recency"
        const val DEFAULT_PAGE = 1
        const val DEFAULT_SIZE = 30
    }
}
