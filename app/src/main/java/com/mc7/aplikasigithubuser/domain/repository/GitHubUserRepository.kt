package com.mc7.aplikasigithubuser.domain.repository

import androidx.lifecycle.LiveData
import com.mc7.aplikasigithubuser.data.Result
import com.mc7.aplikasigithubuser.data.local.entity.FavoriteUser
import com.mc7.aplikasigithubuser.data.remote.response.DetailUserResponse
import com.mc7.aplikasigithubuser.data.remote.response.ItemsItem

interface GitHubUserRepository {
    fun getGitHubUser(username: String): LiveData<Result<List<ItemsItem>>>

    fun getFollowers(username: String): LiveData<Result<List<ItemsItem>>>

    fun getFollowing(username: String): LiveData<Result<List<ItemsItem>>>

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>>

    fun getDetailUserByUsername(username: String): LiveData<Result<DetailUserResponse>>

    fun isFavorite(username: String): LiveData<Boolean>

    suspend fun insert(favoriteUser: FavoriteUser)

    suspend fun delete(favoriteUser: FavoriteUser)
}