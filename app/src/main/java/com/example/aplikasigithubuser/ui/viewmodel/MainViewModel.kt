package com.example.aplikasigithubuser.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aplikasigithubuser.data.repository.UserListRepositoryImp
import com.example.aplikasigithubuser.data.remote.response.ItemsItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userListRepositoryImp: UserListRepositoryImp
) : ViewModel() {

    private var listGitHubUser: ArrayList<ItemsItem>? = null

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

    fun getGitHubUserList(username: String) = userListRepositoryImp.getGitHubUser(username)

    fun saveGitHubUserList(list: ArrayList<ItemsItem>){
        listGitHubUser = list
    }

    fun isFilledGitHubUserList(): Boolean{
        return listGitHubUser != null
    }

    fun getSavedGitHubUserList(): ArrayList<ItemsItem>? {
        return listGitHubUser
    }
}