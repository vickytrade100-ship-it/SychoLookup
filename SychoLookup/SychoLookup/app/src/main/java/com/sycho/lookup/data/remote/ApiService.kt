package com.sycho.lookup.data.remote

import com.sycho.lookup.data.model.LookupResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("lookup/{query}")
    suspend fun lookup(@Path("query") query: String): Response<LookupResponse>
}
