package com.example.dicodingsubmission2.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient(baseUrl:String) {
    val instanceUserList : GetListUsers by lazy {
        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(GetListUsers::class.java)
    }

    val instanceDetailUser: GitHubGetDetailUser by lazy {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(GitHubGetDetailUser::class.java)
    }

    val instanceFollowers: GitHubGetFollowers by lazy {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(GitHubGetFollowers::class.java)
    }

    val instanceFollowing: GitHubGetFollowing by lazy {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(GitHubGetFollowing::class.java)
    }

}