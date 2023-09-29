package com.mc7.aplikasigithubuser.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.mc7.aplikasigithubuser.data.Result
import com.mc7.aplikasigithubuser.data.local.entity.FavoriteUser
import com.mc7.aplikasigithubuser.data.remote.response.DetailUserResponse
import com.mc7.aplikasigithubuser.data.remote.response.ItemsItem
import com.mc7.aplikasigithubuser.domain.repository.GitHubUserRepository

class MockUserListRepository: GitHubUserRepository{

    private val userItems = ArrayList<ItemsItem>()

    override fun getGitHubUser(username: String): LiveData<Result<List<ItemsItem>>> = liveData{

        if (username.isNotEmpty()){
            emit(Result.Success(userItems))
        }else{
            emit(Result.Empty)
        }
    }

    override fun getFollowers(username: String): LiveData<Result<List<ItemsItem>>> {
        TODO("Not yet implemented")
    }

    override fun getFollowing(username: String): LiveData<Result<List<ItemsItem>>> {
        TODO("Not yet implemented")
    }

    override fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> {
        TODO("Not yet implemented")
    }

    override fun getDetailUserByUsername(username: String): LiveData<Result<DetailUserResponse>> {
        TODO("Not yet implemented")
    }

    override fun isFavorite(username: String): LiveData<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun insert(favoriteUser: FavoriteUser) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(favoriteUser: FavoriteUser) {
        TODO("Not yet implemented")
    }
}