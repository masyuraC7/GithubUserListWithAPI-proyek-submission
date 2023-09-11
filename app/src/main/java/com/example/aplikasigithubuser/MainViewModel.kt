package com.example.aplikasigithubuser

import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private var listGitHubUser = ArrayList<ItemsItem>()
    private var errorMessage: String? = null

    fun getListGitHubUser(list: ArrayList<ItemsItem>){
        listGitHubUser = list
    }

    fun getListGitHubUser(): ArrayList<ItemsItem> {
        return listGitHubUser
    }

    fun getErrorMessage(message: String){
        errorMessage = message
    }

    fun getErrorMessage(): String{
        return errorMessage ?: ""
    }
}