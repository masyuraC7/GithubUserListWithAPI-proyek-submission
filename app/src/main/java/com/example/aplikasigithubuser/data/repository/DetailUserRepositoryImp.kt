package com.example.aplikasigithubuser.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.aplikasigithubuser.data.Result
import com.example.aplikasigithubuser.data.remote.response.DetailUserResponse
import com.example.aplikasigithubuser.data.remote.retrofit.ApiService
import com.example.aplikasigithubuser.domain.repository.DetailUserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class DetailUserRepositoryImp @Inject constructor(
    private val apiService: ApiService
): DetailUserRepository {
    private var result = MediatorLiveData<Result<DetailUserResponse>>()
    var detailUser = DetailUserResponse()

    override fun getDetailUserByUsername(username: String): LiveData<Result<DetailUserResponse>> {
        result.value = Result.Loading
        val client = apiService.getDetailUser(username)

        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {

                if (response.isSuccessful) {
                    val item = response.body()

                    if (item != null) {
                        detailUser = item
                    }

                    result.value = Result.Success(detailUser)
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })

        return result
    }
}