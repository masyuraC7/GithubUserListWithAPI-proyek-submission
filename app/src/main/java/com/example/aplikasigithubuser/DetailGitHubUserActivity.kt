package com.example.aplikasigithubuser

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.aplikasigithubuser.apiretrofit.ApiConfig
import com.example.aplikasigithubuser.databinding.ActivityDetailGitHubUserBinding
import com.example.aplikasigithubuser.ui.main.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailGitHubUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailGitHubUserBinding
    private lateinit var viewModel: DetailViewModel
    lateinit var avatarUrl: String
    lateinit var name: String
    lateinit var login: String
    lateinit var followers: String
    lateinit var following: String

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )

        const val KEY_LOGIN = "key_login"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailGitHubUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        val dataUser = intent.getStringExtra(KEY_LOGIN)

        avatarUrl   = viewModel.avatarUrlDetail()
        name        = viewModel.nameDetail()
        login       = viewModel.loginDetail()
        followers   = viewModel.followersDetail()
        following   = viewModel.followingDetail()

        if (login != "") {
            showDetailUserPage(avatarUrl, name, login, followers, following)
            return
        }

        if (dataUser != null) {
            binding.progressBarDetail.visibility = View.VISIBLE

            ApiConfig.getService().getDetailUser(dataUser)
                .enqueue(object : Callback<DetailUserResponse> {
                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>
                ) {
                    if (response.isSuccessful) {
                        binding.progressBarDetail.visibility = View.INVISIBLE

                        val responseDetailUser = response.body()
                        if (responseDetailUser != null) {
                            avatarUrl   = responseDetailUser.avatarUrl ?: ""
                            name        = responseDetailUser.name ?: "default name"
                            login       = responseDetailUser.login ?: "default_login"
                            followers   = "${responseDetailUser.followers.toString()} Followers"
                            following   = "${responseDetailUser.following.toString()} Following"

                            Toast.makeText(this@DetailGitHubUserActivity,
                                "berhasil memuat data detail user", Toast.LENGTH_SHORT).show()

                            viewModel.getDetailUserGitHub(avatarUrl, name, login, followers, following)

                            showDetailUserPage(avatarUrl, name, login, followers, following)
                        }
                    }
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    binding.progressBarDetail.visibility = View.INVISIBLE

                    val errorMessage = "Gagal memuat data, silahkan coba lagi setelah beberapa saat."

                    Toast.makeText(this@DetailGitHubUserActivity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun showDetailUserPage(avatar: String, name: String,
                           login: String, followers: String, following: String){
        Glide.with(this@DetailGitHubUserActivity)
            .load(avatar)
            .circleCrop().into(binding.imageView)

        binding.tvTitle.text = name
        binding.tvFollowers.text = followers
        binding.tvFollowing.text = following

        binding.tvSubtitle.text = login

        val sectionsPagerAdapter = SectionsPagerAdapter(this@DetailGitHubUserActivity)
        sectionsPagerAdapter.setUsername(login)
        val viewPager: ViewPager2 = binding.vpTabs
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tlTabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }
}