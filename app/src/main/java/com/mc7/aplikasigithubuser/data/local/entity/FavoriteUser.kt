package com.mc7.aplikasigithubuser.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "FavoriteUser")
@Parcelize
data class FavoriteUser(
    @field:ColumnInfo(name = "username")
    @field:PrimaryKey(autoGenerate = false)
    var username: String = "",

    @field:ColumnInfo(name = "avatarUrl")
    var avatarUrl: String? = null
) : Parcelable
