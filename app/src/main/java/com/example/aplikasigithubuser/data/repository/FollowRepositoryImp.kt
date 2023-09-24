package com.example.aplikasigithubuser.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.aplikasigithubuser.data.Result
import com.example.aplikasigithubuser.data.remote.response.ItemsItem
import com.example.aplikasigithubuser.data.remote.retrofit.ApiService
import com.example.aplikasigithubuser.domain.repository.FollowRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class FollowRepositoryImp @Inject constructor(
    private val apiService: ApiService
): FollowRepository {
    private var resultFollowers = MediatorLiveData<Result<List<ItemsItem>>>()
    private var resultFollowing = MediatorLiveData<Result<List<ItemsItem>>>()
    val followersList = ArrayList<ItemsItem>()
    val followingList = ArrayList<ItemsItem>()

    override fun getFollowers(username: String): LiveData<Result<List<ItemsItem>>> {
        resultFollowers.value = Result.Loading
        val client = apiService.getFollowersList(username)

        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                if (response.isSuccessful){
                    val responseUserList = response.body()

                    if (followersList.size > 0) followersList.clear()
                    followersList.addAll(responseUserList as ArrayList<ItemsItem>)

                    if (followersList.size <= 0){
                        val errorMsg = "nothing"
                        resultFollowers.value = Result.Error(errorMsg)
                        return
                    }else{
                        resultFollowers.value = Result.Success(followersList)
                    }
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                resultFollowers.value = Result.Error(t.message.toString())
            }
        })

        return resultFollowers
    }

    override fun getFollowing(username: String): LiveData<Result<List<ItemsItem>>> {
        resultFollowing.value = Result.Loading
        val client = apiService.getFollowingList(username)

        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                if (response.isSuccessful){
                    val responseUserList = response.body()

                    if (followingList.size > 0) followingList.clear()
                    followingList.addAll(responseUserList as ArrayList<ItemsItem>)

                    if (followingList.size <= 0){
                        val errorMsg = "nothing"
                        resultFollowing.value = Result.Error(errorMsg)
                        return
                    }else{
                        resultFollowing.value = Result.Success(followingList)
                    }
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                resultFollowing.value = Result.Error(t.message.toString())
            }
        })

        return resultFollowing
    }
}