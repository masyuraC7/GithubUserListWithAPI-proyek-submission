package com.mc7.aplikasigithubuser.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mc7.aplikasigithubuser.data.local.entity.FavoriteUser
import com.mc7.aplikasigithubuser.data.repository.FavoriteUserRepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteUserRepositoryImp: FavoriteUserRepositoryImp
): ViewModel() {

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    private var dataUser = FavoriteUser()

    fun dataUser(username: String, avatarUrl: String){
        dataUser.username = username
        dataUser.avatarUrl = avatarUrl
    }

    fun dataUser(): FavoriteUser{
        return dataUser
    }

    fun isFavorite(isFavExist: Boolean){
        _isFavorite.value = isFavExist
    }

    fun getAllFavUser(): LiveData<List<FavoriteUser>> =
        favoriteUserRepositoryImp.getAllFavoriteUser()

    fun getFavUserByUsername(username: String): LiveData<FavoriteUser> =
        favoriteUserRepositoryImp.getFavoriteUserByUsername(username)


    fun insert(favoriteUser: FavoriteUser) {
        favoriteUserRepositoryImp.insert(favoriteUser)
    }

    fun delete(favoriteUser: FavoriteUser) {
        favoriteUserRepositoryImp.delete(favoriteUser)
    }
}