package com.mc7.aplikasigithubuser.data.remote.retrofit

import com.mc7.aplikasigithubuser.data.remote.response.DetailUserResponse
import com.mc7.aplikasigithubuser.data.remote.response.GitHubResponse
import com.mc7.aplikasigithubuser.data.remote.response.ItemsItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun getListUsers(@Query("q") username: String): Call<GitHubResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<DetailUserResponse>

    @GET("/users/{username}/followers")
    fun getFollowersList(@Path("username") username: String) : Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowingList(@Path("username") username: String) : Call<List<ItemsItem>>
}