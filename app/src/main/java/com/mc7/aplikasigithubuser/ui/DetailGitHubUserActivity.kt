package com.mc7.aplikasigithubuser.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.mc7.aplikasigithubuser.R
import com.mc7.aplikasigithubuser.data.Result
import com.mc7.aplikasigithubuser.data.remote.response.DetailUserResponse
import com.mc7.aplikasigithubuser.databinding.ActivityDetailGitHubUserBinding
import com.mc7.aplikasigithubuser.ui.adaptor.SectionsPagerAdapter
import com.mc7.aplikasigithubuser.ui.viewmodel.DetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailGitHubUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailGitHubUserBinding
    private lateinit var detailUser: DetailUserResponse
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailGitHubUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userLogin = intent.getStringExtra(KEY_LOGIN)

        viewModel.isLoading.observe(this){
            showLoading(it)
        }

        if (viewModel.isFilledDetailUser()){
            viewModel.getSavedDetailUser()?.let { showDetailUser(it) }
        }else{
            if (userLogin != null) {
                viewModel.getDetailUser(userLogin).observe(this){ result ->
                    if (result != null){
                        when (result){
                            is Result.Loading -> {
                                viewModel.isLoading(true)
                            }
                            is Result.Success -> {
                                viewModel.isLoading(false)

                                detailUser = result.data
                                viewModel.saveDetailUser(detailUser)

                                showDetailUser(detailUser)
                            }
                            is Result.Error -> {
                                viewModel.isLoading(false)
                                val msgError = "Terjadi kesalahan" + result.error
                                Toast.makeText(this, msgError, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBarDetail.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    @SuppressLint("SetTextI18n")
    private fun showDetailUser(user: DetailUserResponse){
        Glide.with(this@DetailGitHubUserActivity)
            .load(user.avatarUrl)
            .circleCrop().into(binding.imageView)

        binding.tvTitle.text = user.name
        binding.tvFollowers.text = "${user.followers.toString()} Followers"
        binding.tvFollowing.text = "${user.following.toString()} Following"

        binding.tvSubtitle.text = user.login

        val sectionsPagerAdapter = SectionsPagerAdapter(this@DetailGitHubUserActivity)
        sectionsPagerAdapter.setUsername(user.login.toString())

        val viewPager: ViewPager2 = binding.vpTabs
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = binding.tlTabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )

        const val KEY_LOGIN = "key_login"
    }
}