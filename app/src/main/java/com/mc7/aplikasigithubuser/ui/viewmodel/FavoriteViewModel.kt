package com.mc7.aplikasigithubuser.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mc7.aplikasigithubuser.data.local.entity.FavoriteUser
import com.mc7.aplikasigithubuser.domain.repository.GitHubUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val gitHubUserRepository: GitHubUserRepository
): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<String?>()
    val isError: LiveData<String?> = _isError

    private val _isFilledFavorite = MutableLiveData<List<FavoriteUser>?>()
    val isFilledFavorite: LiveData<List<FavoriteUser>?> = _isFilledFavorite

    fun getAllFavUser() {
        gitHubUserRepository.getAllFavoriteUser().observeForever{
            _isLoading.value = true
            if (it.isNotEmpty()) {
                _isLoading.value = false
                _isError.value = null
                _isFilledFavorite.value = it
            }else{
                _isLoading.value = false
                _isFilledFavorite.value = null
                _isError.value = "Daftar favorite kosong"
            }
        }
    }

    fun delete(favoriteUser: FavoriteUser) {
        viewModelScope.launch {
            gitHubUserRepository.delete(favoriteUser)
        }
    }
}