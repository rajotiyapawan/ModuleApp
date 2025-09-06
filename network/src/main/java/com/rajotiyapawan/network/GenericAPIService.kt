package com.rajotiyapawan.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface GenericAPIService {
    @GET
    suspend fun getCall(@Url url: String): Response<ResponseBody>

    @POST
    suspend fun postCall(@Url url: String, @Body body: Any): Response<ResponseBody>
}