package com.mc7.aplikasigithubuser.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.mc7.aplikasigithubuser.data.Result
import com.mc7.aplikasigithubuser.data.local.entity.FavoriteUser
import com.mc7.aplikasigithubuser.data.local.room.FavUserDao
import com.mc7.aplikasigithubuser.data.remote.response.DetailUserResponse
import com.mc7.aplikasigithubuser.data.remote.response.ItemsItem
import com.mc7.aplikasigithubuser.data.remote.retrofit.ApiService
import com.mc7.aplikasigithubuser.domain.repository.GitHubUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class GitHubUserRepositoryImp @Inject constructor(
    private val apiService: ApiService,
    private val favUserDao: FavUserDao
) : GitHubUserRepository {

    override fun getGitHubUser(username: String):
            LiveData<Result<List<ItemsItem>>> = liveData {
        emit(Result.Loading)

        try {
            if (username.isNotEmpty()){
                val response = apiService.getListUsers(username)
                val items = response.items

                if (items != null)
                    if (items.isNotEmpty())
                        emit(Result.Success(items))
                    else
                        emit(Result.Empty)
            }else{
                emit(Result.Empty)
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    override fun getDetailUserByUsername(username: String):
            LiveData<Result<DetailUserResponse>> = liveData {
        emit(Result.Loading)

        try {
            val response = apiService.getDetailUser(username)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    override fun isFavorite(username: String): LiveData<Boolean> =
        favUserDao.isFavorite(username)

    override suspend fun insert(favoriteUser: FavoriteUser) {
        coroutineScope {
            launch(Dispatchers.IO) {
                favUserDao.insert(favoriteUser)
            }
        }
    }

    override suspend fun delete(favoriteUser: FavoriteUser) {
        coroutineScope {
            launch(Dispatchers.IO) {
                favUserDao.delete(favoriteUser)
            }
        }
    }

    override fun getFollowers(username: String):
            LiveData<Result<List<ItemsItem>>> = liveData {
        emit(Result.Loading)

        try {
            val response = apiService.getFollowersList(username)

            if (response.isNotEmpty())
                emit(Result.Success(response))
            else
                emit(Result.Empty)

        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    override fun getFollowing(username: String):
            LiveData<Result<List<ItemsItem>>> = liveData {
        emit(Result.Loading)

        try {
            val response = apiService.getFollowingList(username)

            if (response.isNotEmpty())
                emit(Result.Success(response))
            else
                emit(Result.Empty)

        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    override fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> {
        return favUserDao.getAllFavoriteUser()
    }
}