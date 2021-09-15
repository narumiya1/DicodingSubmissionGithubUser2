package com.example.dicodingsubmission2.network

import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface GetListUsers {
    @GET("search/users")
    fun getListUsers(
        @Header("Authorization") accessToken: String,
        @Query("q") user: String?
    ): Call<JsonElement>
}


interface GitHubGetDetailUser {
    @GET("users/{username}")
    fun getDetailUser(
        @Header("Authorization") accessToken: String,
        @Path("username") username: String?
    ): Call<JsonElement>
}

interface GitHubGetFollowers {
    @GET("users/{username}/followers")
    fun getFollowers(
        @Header("Authorization") accessToken: String,
        @Path("username") username: String?
    ): Call<JsonElement>
}

interface GitHubGetFollowing {
    @GET("users/{username}/following")
    fun getFollowing(
        @Header("Authorization") accessToken: String,
        @Path("username") username: String?
    ): Call<JsonElement>
}

