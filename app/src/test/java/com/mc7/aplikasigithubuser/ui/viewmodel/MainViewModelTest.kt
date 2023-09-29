package com.mc7.aplikasigithubuser.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mc7.aplikasigithubuser.MainCoroutineRule
import com.mc7.aplikasigithubuser.data.remote.response.ItemsItem
import com.mc7.aplikasigithubuser.data.repository.MockUserListRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        viewModel = MainViewModel(MockUserListRepository())
    }

    @Test
    fun isLoadingTrue() {
        viewModel.isLoading(true)
        val getLoading = viewModel.isLoading.value
        assertEquals(true, getLoading)
    }

    @Test
    fun isLoadingFalse() {
        viewModel.isLoading(false)
        val getLoading = viewModel.isLoading.value
        assertEquals(false, getLoading)
    }

    @Test
    fun isErrorEmptyTrue() {
        viewModel.isError("")
        val getError: Boolean = viewModel.isError.value?.isEmpty() == true
        assertEquals(true, getError)
    }

    @Test
    fun isErrorEmptyFalse() {
        viewModel.isError("Error Message")
        val getError: Boolean = viewModel.isError.value?.isEmpty() == true
        assertEquals(false, getError)
    }

    @Test
    fun isFilledUserTrue() {
        val userList = ArrayList<ItemsItem>()
        viewModel.isFilledUser(userList)

        val getUser: Boolean = viewModel.isFilledUser.value != null

        assertEquals(true, getUser)
    }

    @Test
    fun isFilledUserFalse() {
        viewModel.isFilledUser(null)
        val getUser: Boolean = viewModel.isFilledUser.value != null

        assertEquals(false, getUser)
    }

    @Test
    fun isGetGitHubUserIfUsernameNotEmptyTrue() {
        val username = "masyura"
        viewModel.getGitHubUserList(username)

        val getError: Boolean = viewModel.isError.value == null

        assertEquals(true, getError)
    }

    @Test
    fun isGetGitHubUserIfUsernameNotEmptyFalse() {
        val username = ""

        viewModel.getGitHubUserList(username)

        val getError: Boolean = viewModel.isError.value == null

        assertEquals(false, getError)
    }
}