package com.example.aplikasigithubuser

import androidx.lifecycle.ViewModel

class DetailViewModel : ViewModel() {
    private var followerListUser = ArrayList<ItemsItem>()
    private var followingListUser = ArrayList<ItemsItem>()
    private var avatarUrlDetail: String = ""
    private var nameDetail: String = ""
    private var loginDetail: String = ""
    private var followersDetail: String = ""
    private var followingDetail: String = ""
    private var errorMessage: String? = null

    fun getFollowersUserList(list: ArrayList<ItemsItem>){
        followerListUser = list
    }

    fun getFollowersUserList(): ArrayList<ItemsItem> {
        return followerListUser
    }

    fun getFollowingUserList(list: ArrayList<ItemsItem>){
        followingListUser = list
    }

    fun getFollowingUserList(): ArrayList<ItemsItem> {
        return followingListUser
    }

    fun getDetailUserGitHub(avatar: String, name: String,
                            login: String, followers: String, following: String){
        avatarUrlDetail = avatar
        nameDetail = name
        loginDetail = login
        followersDetail = followers
        followingDetail = following
    }

    fun avatarUrlDetail(): String {
        return avatarUrlDetail
    }

    fun nameDetail(): String {
        return nameDetail
    }

    fun loginDetail(): String {
        return loginDetail
    }

    fun followersDetail(): String {
        return followersDetail
    }

    fun followingDetail(): String {
        return followingDetail
    }

    fun getErrorMessage(message: String){
        errorMessage = message
    }

    fun getErrorMessage(): String{
        return errorMessage ?: ""
    }
}