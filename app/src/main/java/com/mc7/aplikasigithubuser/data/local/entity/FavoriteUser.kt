package com.mc7.aplikasigithubuser.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FavoriteUser")
data class FavoriteUser(
    @field:ColumnInfo(name = "username")
    @field:PrimaryKey(autoGenerate = false)
    var username: String = "",

    @field:ColumnInfo(name = "avatarUrl")
    var avatarUrl: String? = null
)
