package com.mc7.aplikasigithubuser.ui

import android.annotation.SuppressLint
import android.content.Intent
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
import com.mc7.aplikasigithubuser.data.local.entity.FavoriteUser
import com.mc7.aplikasigithubuser.data.remote.response.DetailUserResponse
import com.mc7.aplikasigithubuser.databinding.ActivityDetailGitHubUserBinding
import com.mc7.aplikasigithubuser.ui.adapter.SectionsPagerAdapter
import com.mc7.aplikasigithubuser.ui.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailGitHubUserActivity : AppCompatActivity() {

    // binding
    private lateinit var binding: ActivityDetailGitHubUserBinding

    // data class
    private var dataUser = FavoriteUser()

    // view-model
    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailGitHubUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get username from main activity
        val userLogin = intent.getStringExtra(KEY_LOGIN).toString()

        // set loading
        detailViewModel.isLoading.observe(this) { showLoading(it) }

        // set show error
        detailViewModel.isError.observe(this) { showError(it) }

        // get detail user
        detailViewModel.getDetailUser(userLogin)
        detailViewModel.isFilledDetail.observe(this) { item ->
            if (item != null) {
                showDetailUser(item)
                dataUser.username = item.login.toString()
                dataUser.avatarUrl = item.avatarUrl
            }
        }

        // set fab button change
        detailViewModel.isFavorite(userLogin).observe(this) { isExist ->
            // set insert and delete favorite
            if (isExist) {
                binding.fabFavUser
                    .setImageResource(R.drawable.baseline_favorite_24_full)
                binding.fabFavUser.setOnClickListener {
                    detailViewModel.delete(dataUser)
                    Toast.makeText(
                        this, "Berhasil dihapus dari daftar favorite",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                binding.fabFavUser
                    .setImageResource(R.drawable.baseline_favorite_border_24)
                binding.fabFavUser.setOnClickListener {
                    detailViewModel.insert(dataUser)
                    Toast.makeText(
                        this, "Berhasil ditambahkan ke daftar favorite",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun showError(errorMsg: String?) {
        Toast.makeText(this@DetailGitHubUserActivity, errorMsg, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarDetail.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    // adapter show
    @SuppressLint("SetTextI18n")
    private fun showDetailUser(user: DetailUserResponse) {
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

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val resultIntent = Intent()
        resultIntent.putExtra(EXTRA_UPDATE_FAVORITE, "update")
        setResult(RESULT_CODE, resultIntent)
        finish()
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )

        const val KEY_LOGIN = "key_login"
        const val EXTRA_UPDATE_FAVORITE = "extra_update_favorite"
        const val RESULT_CODE = 110
    }
}