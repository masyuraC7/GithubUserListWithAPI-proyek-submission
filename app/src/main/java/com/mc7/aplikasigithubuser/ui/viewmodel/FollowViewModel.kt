package com.mc7.aplikasigithubuser.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mc7.aplikasigithubuser.data.remote.response.ItemsItem
import com.mc7.aplikasigithubuser.data.repository.FollowRepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FollowViewModel @Inject constructor(
    private val followRepositoryImp: FollowRepositoryImp
): ViewModel() {
    private var followingList: ArrayList<ItemsItem>? = null
    private var followersList: ArrayList<ItemsItem>? = null

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<String>()
    val isError: LiveData<String> = _isError

    fun isLoading(isLoadings: Boolean){
        _isLoading.value = isLoadings
    }

    fun isError(isErrors: String){
        _isError.value = isErrors
    }

    fun getFollowers(username: String) = followRepositoryImp.getFollowers(username)

    fun getFollowing(username: String) = followRepositoryImp.getFollowing(username)

    fun saveFollowersList(followers: ArrayList<ItemsItem>){
        followersList = followers
    }

    fun isFilledFollowersList(): Boolean{
        return followersList != null
    }

    fun getSavedFollowersList(): ArrayList<ItemsItem>? {
        return followersList
    }

    fun saveFollowingList(following: ArrayList<ItemsItem>){
        followingList = following
    }

    fun isFilledFollowingList(): Boolean{
        return followingList != null
    }

    fun getSavedFollowingList(): ArrayList<ItemsItem>? {
        return followingList
    }
}