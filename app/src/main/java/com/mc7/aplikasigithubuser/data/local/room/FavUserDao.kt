package com.mc7.aplikasigithubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mc7.aplikasigithubuser.data.local.entity.FavoriteUser

@Dao
interface FavUserDao {

    @Query("SELECT * FROM FavoriteUser WHERE username = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteUser(favUser: FavoriteUser)

    @Query("DELETE FROM FavoriteUser WHERE username = :username")
    fun deleteFavoriteUserByUsername(username: String)
}