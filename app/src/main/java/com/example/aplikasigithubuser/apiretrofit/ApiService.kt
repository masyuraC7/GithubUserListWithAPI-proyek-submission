package com.example.aplikasigithubuser.apiretrofit

import com.example.aplikasigithubuser.DetailUserResponse
import com.example.aplikasigithubuser.GitHubResponse
import com.example.aplikasigithubuser.ItemsItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getListUsers(@Query("q") username: String): Call<GitHubResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getDetailUser(@Path("username") username: String): Call<DetailUserResponse>

    @GET("/users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowersList(@Path("username") username: String) : Call<List<ItemsItem>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowingList(@Path("username") username: String) : Call<List<ItemsItem>>
}