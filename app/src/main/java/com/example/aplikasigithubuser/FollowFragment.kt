package com.example.aplikasigithubuser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasigithubuser.apiretrofit.ApiConfig
import com.example.aplikasigithubuser.databinding.FragmentFollowBinding
import retrofit2.Callback
import retrofit2.Response

class FollowFragment : Fragment() {
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private var followersUserList = ArrayList<ItemsItem>()
    private var followingUserList = ArrayList<ItemsItem>()
    private lateinit var viewModel: DetailViewModel
    private var position: Int = 0
    private var username: String? = null
    private lateinit var errorMessage: String

    companion object {
        const val ARG_USERNAME = "arg_username"
        const val ARG_POSITION = "arg_position"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }

        val userLog: String = username ?: "airporad"

        followersUserList = viewModel.getFollowersUserList()
        followingUserList = viewModel.getFollowingUserList()

        if (position == 1){
            if (viewModel.getErrorMessage().isNotEmpty()){
                binding.tvErrorMessage.text = viewModel.getErrorMessage()
                return
            }else{
                binding.tvErrorMessage.visibility = View.INVISIBLE
            }

            if (followersUserList.size > 0){
                binding.progressBar.visibility = View.INVISIBLE
                showRecyclerViewList(followersUserList)
                return
            }
        }else{
            if (viewModel.getErrorMessage().isNotEmpty()){
                binding.tvErrorMessage.text = viewModel.getErrorMessage()
                return
            }else{
                binding.tvErrorMessage.visibility = View.INVISIBLE
            }

            if (followingUserList.size > 0){
                binding.progressBar.visibility = View.INVISIBLE
                showRecyclerViewList(followingUserList)
                return
            }
        }

        if (position == 1) {
            val list = ArrayList<ItemsItem>()

            ApiConfig.getService().getFollowersList(userLog)
                .enqueue(object : Callback<List<ItemsItem>> {
                    override fun onResponse(
                        call: retrofit2.Call<List<ItemsItem>>,
                        response: Response<List<ItemsItem>>
                    ) {
                        if (response.isSuccessful){
                            val responseUserList = response.body()

                            list.addAll(responseUserList as ArrayList<ItemsItem>)

                            if (list.size <= 0) {
                                errorMessage = "Data following user ini tidak ditemukan"
                                binding.tvErrorMessage.text = errorMessage
                                viewModel.getErrorMessage(errorMessage)
                                Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show()

                                binding.progressBar.visibility = View.VISIBLE
                                binding.tvErrorMessage.visibility = View.VISIBLE
                                return
                            } else {
                                errorMessage = ""
                                binding.tvErrorMessage.visibility = View.INVISIBLE
                            }

                            Toast.makeText(requireActivity(),
                                "berhasil memuat data followers", Toast.LENGTH_SHORT).show()

                            binding.progressBar.visibility = View.INVISIBLE
                            followersUserList.addAll(list)
                            viewModel.getFollowersUserList(followersUserList)

                                showRecyclerViewList(followersUserList)
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<List<ItemsItem>>, t: Throwable) {
                        binding.tvErrorMessage.text = t.message
                    }

                })
        }else{
            val list = ArrayList<ItemsItem>()

            ApiConfig.getService().getFollowingList(userLog)
                .enqueue(object : Callback<List<ItemsItem>> {
                    override fun onResponse(
                        call: retrofit2.Call<List<ItemsItem>>,
                        response: Response<List<ItemsItem>>
                    ) {
                        if (response.isSuccessful){
                            val responseUserList = response.body()

                            list.addAll(responseUserList as ArrayList<ItemsItem>)

                            if (list.size <= 0) {
                                errorMessage = "Data following user ini tidak ditemukan"
                                binding.tvErrorMessage.text = errorMessage
                                viewModel.getErrorMessage(errorMessage)
                                Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show()

                                binding.progressBar.visibility = View.VISIBLE
                                binding.tvErrorMessage.visibility = View.VISIBLE
                                return
                            } else {
                                errorMessage = ""
                                binding.tvErrorMessage.visibility = View.INVISIBLE
                            }

                            Toast.makeText(requireActivity(),
                                "berhasil memuat data following", Toast.LENGTH_SHORT).show()

                            binding.progressBar.visibility = View.INVISIBLE
                            followingUserList.addAll(list)
                            viewModel.getFollowingUserList(followingUserList)

                            showRecyclerViewList(followingUserList)
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<List<ItemsItem>>, t: Throwable) {
                        binding.tvErrorMessage.text = t.message
                    }

                })
        }
    }

    private fun showRecyclerViewList(list: ArrayList<ItemsItem>) {
        binding.rvFollow.layoutManager = LinearLayoutManager(requireActivity())
        val listAdapter = ListGitHubUserAdapter(list)
        binding.rvFollow.adapter = listAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}