package com.prmto.artbookhilttesting.api

import com.prmto.artbookhilttesting.model.ImageResponse
import com.prmto.artbookhilttesting.util.Util
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitAPI {

    @GET("/api/")
    suspend fun imageSearch(
        @Query("key") apiKey: String = Util.API_KEY,
        @Query("q") searchQuery: String

    ): Response<ImageResponse>
}