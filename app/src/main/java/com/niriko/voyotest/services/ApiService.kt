package com.niriko.voyotest.services

import com.niriko.voyotest.model.ApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {
    @GET
    fun fetchUrls(@Url fullUrl: String): Call<ApiResponse>
}
