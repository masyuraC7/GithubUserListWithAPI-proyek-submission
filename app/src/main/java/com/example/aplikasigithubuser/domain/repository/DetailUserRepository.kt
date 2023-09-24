package com.example.aplikasigithubuser.domain.repository

import androidx.lifecycle.LiveData
import com.example.aplikasigithubuser.data.Result
import com.example.aplikasigithubuser.data.remote.response.DetailUserResponse

interface DetailUserRepository {
    fun getDetailUserByUsername(username: String): LiveData<Result<DetailUserResponse>>
}