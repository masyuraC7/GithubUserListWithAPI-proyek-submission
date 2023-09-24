package com.mc7.aplikasigithubuser.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mc7.aplikasigithubuser.data.local.entity.FavoriteUser

@Database(entities = [FavoriteUser::class], version = 1, exportSchema = false)
abstract class FavUserDatabase : RoomDatabase() {

    abstract fun favUserDao(): FavUserDao

}