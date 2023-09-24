package com.example.aplikasigithubuser.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.aplikasigithubuser.data.Result
import com.example.aplikasigithubuser.data.remote.response.GitHubResponse
import com.example.aplikasigithubuser.data.remote.response.ItemsItem
import com.example.aplikasigithubuser.data.remote.retrofit.ApiService
import com.example.aplikasigithubuser.domain.repository.UserListRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class UserListRepositoryImp @Inject constructor(
    private val apiService: ApiService
): UserListRepository {
    private var result = MediatorLiveData<Result<List<ItemsItem>>>()
    val gitHubUserList = ArrayList<ItemsItem>()

    override fun getGitHubUser(username: String): LiveData<Result<List<ItemsItem>>> {
        result.value = Result.Loading
        val client = apiService.getListUsers(username)

        client.enqueue(object : Callback<GitHubResponse> {
            override fun onResponse(
                call: Call<GitHubResponse>,
                response: Response<GitHubResponse>
            ) {

                if (response.isSuccessful) {
                    val items = response.body()?.items

                    if (gitHubUserList.size > 0) gitHubUserList.clear()

                    gitHubUserList.addAll(items as ArrayList<ItemsItem>)

                    result.value = Result.Success(gitHubUserList)
                }
            }

            override fun onFailure(call: Call<GitHubResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })

        return result
    }
}