package com.mc7.aplikasigithubuser.domain.repository

import androidx.lifecycle.LiveData
import com.mc7.aplikasigithubuser.data.Result
import com.mc7.aplikasigithubuser.data.remote.response.ItemsItem

interface UserListRepository {
    fun getGitHubUser(username: String): LiveData<Result<List<ItemsItem>>>
}