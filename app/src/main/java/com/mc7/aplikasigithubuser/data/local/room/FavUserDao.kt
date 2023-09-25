package com.mc7.aplikasigithubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mc7.aplikasigithubuser.data.local.entity.FavoriteUser

@Dao
interface FavUserDao {

    @Query("SELECT * from FavoriteUser ORDER BY username ASC")
    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>>

    @Query("SELECT * FROM FavoriteUser WHERE username = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favUser: FavoriteUser)

    @Delete
    fun delete(favUser: FavoriteUser)
}