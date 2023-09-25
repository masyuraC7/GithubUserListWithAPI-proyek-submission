package com.mc7.aplikasigithubuser.domain.repository

import androidx.lifecycle.LiveData
import com.mc7.aplikasigithubuser.data.local.entity.FavoriteUser

interface FavoriteUserRepository {

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>>

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser>

    fun insert(favoriteUser: FavoriteUser)

    fun delete(favoriteUser: FavoriteUser)
}