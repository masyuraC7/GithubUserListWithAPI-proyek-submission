package com.example.aplikasigithubuser.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aplikasigithubuser.data.remote.response.DetailUserResponse
import com.example.aplikasigithubuser.data.repository.DetailUserRepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val detailUserRepositoryImp: DetailUserRepositoryImp
) : ViewModel() {

    private var detailUser: DetailUserResponse? = null

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun isLoading(isLoadings: Boolean){
        _isLoading.value = isLoadings
    }

    fun getDetailUser(username: String) = detailUserRepositoryImp.getDetailUserByUsername(username)

    fun saveDetailUser(user: DetailUserResponse){
        detailUser = user
    }

    fun isFilledDetailUser(): Boolean{
        return detailUser != null
    }

    fun getSavedDetailUser(): DetailUserResponse? {
        return detailUser
    }
}