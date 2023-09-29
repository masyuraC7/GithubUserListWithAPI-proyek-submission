package com.mc7.aplikasigithubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mc7.aplikasigithubuser.databinding.FragmentFollowBinding
import com.mc7.aplikasigithubuser.ui.adapter.GitHubUserListAdapter
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

        val userLog: String = username.toString()

        binding.rvFollow.layoutManager = LinearLayoutManager(requireActivity())
        val listAdapter = GitHubUserListAdapter{ item ->
            val intentToDetailActivity =
                Intent(requireActivity(), DetailGitHubUserActivity::class.java)
            intentToDetailActivity.putExtra("key_login", item.login)
            startActivity(intentToDetailActivity)
        }
        binding.rvFollow.adapter = listAdapter

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.isError.observe(viewLifecycleOwner) {
            showError(it)
        }

        if (position == 1) {
            viewModel.getFollowers(userLog)
            viewModel.isFilledFollowers.observe(viewLifecycleOwner){
                listAdapter.submitList(it)
            }
        } else {
            viewModel.getFollowing(userLog)
            viewModel.isFilledFollowing.observe(viewLifecycleOwner){
                listAdapter.submitList(it)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(isError: String?) {
        binding.tvErrorMessage.visibility = if (isError != null) View.VISIBLE else View.GONE

        binding.tvErrorMessage.text = isError
    }

    companion object {
        const val ARG_USERNAME = "arg_username"
        const val ARG_POSITION = "arg_position"
    }
}