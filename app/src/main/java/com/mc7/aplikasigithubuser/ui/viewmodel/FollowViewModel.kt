package com.mc7.aplikasigithubuser.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mc7.aplikasigithubuser.data.Result
import com.mc7.aplikasigithubuser.data.remote.response.ItemsItem
import com.mc7.aplikasigithubuser.domain.repository.GitHubUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FollowViewModel @Inject constructor(
    private val gitHubUserRepository: GitHubUserRepository
): ViewModel() {
    private val _isFilledFollowers = MutableLiveData<List<ItemsItem>?>()
    val isFilledFollowers: LiveData<List<ItemsItem>?> = _isFilledFollowers

    private val _isFilledFollowing = MutableLiveData<List<ItemsItem>?>()
    val isFilledFollowing: LiveData<List<ItemsItem>?> = _isFilledFollowing

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<String?>()
    val isError: LiveData<String?> = _isError

    fun getFollowers(username: String) {
        gitHubUserRepository.getFollowers(username).observeForever{ result ->
            when (result) {
                is Result.Loading -> {
                    _isLoading.value = true
                }

                is Result.Success -> {
                    _isLoading.value = false
                    _isError.value = null
                    _isFilledFollowers.value = result.data
                }

                is Result.Error -> {
                    _isLoading.value = false
                    val msgError = result.error
                    _isError.value = msgError
                }

                is Result.Empty -> {
                    _isLoading.value = false
                    _isFilledFollowers.value = null
                    val msgError = "Data Followers tidak ditemukan"
                    _isError.value = msgError
                }
            }
        }
    }

    fun getFollowing(username: String) {
        gitHubUserRepository.getFollowing(username).observeForever{ result ->
            when (result) {
                is Result.Loading -> {
                    _isLoading.value = true
                }

                is Result.Success -> {
                    _isLoading.value = false
                    _isError.value = null
                    _isFilledFollowing.value = result.data
                }

                is Result.Error -> {
                    _isLoading.value = false
                    val msgError = result.error
                    _isError.value = msgError
                }

                is Result.Empty -> {
                    _isLoading.value = false
                    _isFilledFollowing.value = null
                    val msgError = "Data following tidak ditemukan"
                    _isError.value = msgError
                }
            }
        }
    }
}