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
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mc7.aplikasigithubuser.R
import com.mc7.aplikasigithubuser.data.Result
import com.mc7.aplikasigithubuser.data.local.entity.FavoriteUser
import com.mc7.aplikasigithubuser.data.remote.response.DetailUserResponse
import com.mc7.aplikasigithubuser.databinding.ActivityDetailGitHubUserBinding
import com.mc7.aplikasigithubuser.ui.adaptor.SectionsPagerAdapter
import com.mc7.aplikasigithubuser.ui.viewmodel.DetailViewModel
import com.mc7.aplikasigithubuser.ui.viewmodel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailGitHubUserActivity : AppCompatActivity() {

    // binding
    private lateinit var binding: ActivityDetailGitHubUserBinding

    // data class
    private lateinit var detailUser: DetailUserResponse
    private var dataUser = FavoriteUser()

    // view-model
    private val detailViewModel: DetailViewModel by viewModels()
    private val favoriteViewModel: FavoriteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailGitHubUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get username from main activity
        val userLogin = intent.getStringExtra(KEY_LOGIN)

        // set loading
        detailViewModel.isLoading.observe(this){ showLoading(it) }

        // get detail user
        if (detailViewModel.isFilledDetailUser()){
            detailViewModel.getSavedDetailUser()?.let {
                showDetailUser(it)
                dataUser.username = it.login.toString()
                dataUser.avatarUrl = it.avatarUrl
            }
        }else{
            if (userLogin != null) {
                detailViewModel.getDetailUser(userLogin).observe(this){ result ->
                    if (result != null){
                        when (result){
                            is Result.Loading -> {
                                detailViewModel.isLoading(true)
                            }
                            is Result.Success -> {
                                detailViewModel.isLoading(false)

                                detailUser = result.data
                                detailViewModel.saveDetailUser(detailUser)

                                favoriteViewModel.dataUser(
                                    detailUser.login.toString(), detailUser.avatarUrl.toString()
                                )

                                showDetailUser(detailUser)
                            }
                            is Result.Error -> {
                                detailViewModel.isLoading(false)
                                val msgError = "Terjadi kesalahan" + result.error
                                Toast.makeText(this, msgError, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }

            dataUser = favoriteViewModel.dataUser()
        }

        // set favorite fab button
        if (userLogin != null) {
            favoriteViewModel.getFavUserByUsername(userLogin)
                .observe(this){ item ->
                    if (item != null){
                        binding.fabFavUser
                            .setImageResource(R.drawable.baseline_favorite_24_full_white)
                        favoriteViewModel.isFavorite(true)
                    }else{
                        binding.fabFavUser
                            .setImageResource(R.drawable.baseline_favorite_border_24_white)
                        favoriteViewModel.isFavorite(false)
                    }
                }
        }

        // set insert and delete favorite
        binding.fabFavUser.setOnClickListener {
            if (favoriteViewModel.isFavorite.value == true){
                favoriteViewModel.delete(dataUser)

                Toast.makeText(this, "Berhasil dihapus dari favorite",
                    Toast.LENGTH_SHORT).show()

                binding.fabFavUser.setImageResource(R.drawable.baseline_favorite_border_24_white)
            }else{
                favoriteViewModel.insert(dataUser)

                Toast.makeText(this, "Berhasil ditambahkan ke favorite",
                    Toast.LENGTH_SHORT).show()

                binding.fabFavUser.setImageResource(R.drawable.baseline_favorite_24_full_white)
            }
        }
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBarDetail.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    // adapter show
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