package com.mc7.aplikasigithubuser.domain.repository

import androidx.lifecycle.LiveData
import com.mc7.aplikasigithubuser.data.Result
import com.mc7.aplikasigithubuser.data.remote.response.DetailUserResponse

interface DetailUserRepository {
    fun getDetailUserByUsername(username: String): LiveData<Result<DetailUserResponse>>
}