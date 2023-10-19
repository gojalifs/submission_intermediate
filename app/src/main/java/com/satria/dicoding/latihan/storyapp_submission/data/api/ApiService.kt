package com.satria.dicoding.latihan.storyapp_submission.data.api

import com.satria.dicoding.latihan.storyapp_submission.model.api_response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name:String,
        @Field("email") email:String,
        @Field("password") password:String
    ) : RegisterResponse
}