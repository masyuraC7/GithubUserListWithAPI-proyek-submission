package com.mc7.aplikasigithubuser.data.repository

import androidx.lifecycle.LiveData
import com.mc7.aplikasigithubuser.data.local.entity.FavoriteUser
import com.mc7.aplikasigithubuser.data.local.room.FavUserDao
import com.mc7.aplikasigithubuser.domain.repository.FavoriteUserRepository
import com.mc7.aplikasigithubuser.utils.AppExecutors
import javax.inject.Inject

class FavoriteUserRepositoryImp @Inject constructor(
    private val favUserDao: FavUserDao,
    private val appExecutors: AppExecutors
): FavoriteUserRepository {

    override fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> =
        favUserDao.getAllFavoriteUser()

    override fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> =
        favUserDao.getFavoriteUserByUsername(username)

    override fun insert(favoriteUser: FavoriteUser) =
        appExecutors.diskIO.execute{ favUserDao.insert(favoriteUser) }

    override fun delete(favoriteUser: FavoriteUser) =
        appExecutors.diskIO.execute{ favUserDao.delete(favoriteUser) }
}