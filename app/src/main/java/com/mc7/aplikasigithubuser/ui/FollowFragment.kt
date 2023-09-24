package com.mc7.aplikasigithubuser.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mc7.aplikasigithubuser.data.Result
import com.mc7.aplikasigithubuser.data.remote.response.ItemsItem
import com.mc7.aplikasigithubuser.databinding.FragmentFollowBinding
import com.mc7.aplikasigithubuser.ui.adaptor.GitHubUserListAdapter
import com.mc7.aplikasigithubuser.ui.viewmodel.FollowViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding
    private val viewModel: FollowViewModel by viewModels()
    private var position: Int = 0
    private var username: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // getting tab position and username
        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }

        val userLog: String = username ?: "airporad"

        viewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }

        viewModel.isError.observe(viewLifecycleOwner){
            showError(it)
        }

        if (position == 1) {
            if (viewModel.isFilledFollowersList()){
                viewModel.getSavedFollowersList()?.let { showFollowList(it) }
            }else{
                getFollowersList(userLog)
            }
        }else{
            if (viewModel.isFilledFollowingList()){
                viewModel.getSavedFollowingList()?.let { showFollowList(it) }
            }else{
                getFollowingList(userLog)
            }
        }
    }

    private fun getFollowersList(userLog: String){
        viewModel.getFollowers(userLog).observe(viewLifecycleOwner){ result ->
            if (result != null){
                when (result) {
                    is Result.Loading -> {
                        viewModel.isLoading(true)
                    }
                    is Result.Success -> {
                        if (result.data.isEmpty()){
                            val errorMsg = "Followers tidak ditemukan"
                            viewModel.isError(errorMsg)
                        }else{
                            viewModel.isError("")
                        }

                        viewModel.isLoading(false)

                        val followersList = ArrayList<ItemsItem>()

                        followersList.addAll(result.data)

                        viewModel.saveFollowersList(followersList)
                        showFollowList(followersList)
                    }
                    is Result.Error -> {
                        viewModel.isLoading(false)
                        val isNothing = "nothing"
                        val msgError =
                            if (result.error == isNothing)
                                "Data followers user ini tidak ditemukan"
                            else
                                "Terjadi kesalahan" + result.error
                        viewModel.isError(msgError)
                    }
                }
            }else{
                val errorMsg = "Gagal memuat API"
                viewModel.isError(errorMsg)
            }
        }
    }

    private fun getFollowingList(userLog: String){
        viewModel.getFollowing(userLog).observe(viewLifecycleOwner){ result ->
            if (result != null){
                when (result) {
                    is Result.Loading -> {
                        viewModel.isLoading(true)
                    }
                    is Result.Success -> {
                        if (result.data.isEmpty()){
                            val errorMsg = "Following tidak ditemukan"
                            viewModel.isError(errorMsg)
                        }else{
                            viewModel.isError("")
                        }

                        viewModel.isLoading(false)

                        val followersList = ArrayList<ItemsItem>()

                        followersList.addAll(result.data)

                        viewModel.saveFollowingList(followersList)
                        showFollowList(followersList)
                    }
                    is Result.Error -> {
                        viewModel.isLoading(false)
                        val isNothing = "nothing"
                        val msgError =
                            if (result.error == isNothing)
                                "Data following user ini tidak ditemukan"
                            else
                                "Terjadi kesalahan" + result.error
                        viewModel.isError(msgError)
                    }
                }
            }else{
                val errorMsg = "Gagal memuat API"
                viewModel.isError(errorMsg)
            }
        }
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(isError: String){
        binding.tvErrorMessage.visibility = if (isError.isNotEmpty()) View.VISIBLE else View.GONE

        binding.tvErrorMessage.text = isError
    }

    private fun showFollowList(list: ArrayList<ItemsItem>) {
        binding.rvFollow.layoutManager = LinearLayoutManager(requireActivity())
        val listAdapter = GitHubUserListAdapter(list)
        binding.rvFollow.adapter = listAdapter
    }

    companion object {
        const val ARG_USERNAME = "arg_username"
        const val ARG_POSITION = "arg_position"
    }
}