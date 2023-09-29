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
class MainViewModel @Inject constructor(
    private val gitHubUserRepository: GitHubUserRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<String?>()
    val isError: LiveData<String?> = _isError

    private val _isFilledUser = MutableLiveData<List<ItemsItem>?>()
    val isFilledUser: LiveData<List<ItemsItem>?> = _isFilledUser

    init {
        getGitHubUserList("masyura")
    }

    fun isFilledUser(isFilled: List<ItemsItem>?){
        _isFilledUser.value = isFilled
    }

    fun isLoading(isLoadings: Boolean){
        _isLoading.value = isLoadings
    }

    fun isError(isErrors: String){
        _isError.value = isErrors
    }

    fun getGitHubUserList(username: String) {
        gitHubUserRepository.getGitHubUser(username).observeForever{ result ->
            when (result) {
                is Result.Loading -> {
                    _isLoading.value = true
                }

                is Result.Success -> {
                    _isLoading.value = false
                    _isError.value = null
                    _isFilledUser.value = result.data
                }

                is Result.Error -> {
                    _isLoading.value = false
                    _isFilledUser.value = null
                    val msgError = result.error
                    _isError.value = msgError
                }

                is Result.Empty -> {
                    _isLoading.value = false
                    _isFilledUser.value = null
                    val msgError = "Data user tidak ditemukan"
                    _isError.value = msgError
                }
            }
        }
    }

}