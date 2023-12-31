package com.mc7.aplikasigithubuser.data.remote.retrofit

import com.mc7.aplikasigithubuser.data.remote.response.DetailUserResponse
import com.mc7.aplikasigithubuser.data.remote.response.GitHubResponse
import com.mc7.aplikasigithubuser.data.remote.response.ItemsItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    suspend fun getListUsers(@Query("q") username: String): GitHubResponse

    @GET("users/{username}")
    suspend fun getDetailUser(@Path("username") username: String): DetailUserResponse

    @GET("/users/{username}/followers")
    suspend fun getFollowersList(@Path("username") username: String) : List<ItemsItem>

    @GET("users/{username}/following")
    suspend fun getFollowingList(@Path("username") username: String) : List<ItemsItem>
}