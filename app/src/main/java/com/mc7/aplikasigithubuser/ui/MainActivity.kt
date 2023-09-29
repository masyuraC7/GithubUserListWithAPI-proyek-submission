package com.mc7.aplikasigithubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mc7.aplikasigithubuser.R
import com.mc7.aplikasigithubuser.databinding.ActivityMainBinding
import com.mc7.aplikasigithubuser.ui.adapter.GitHubUserListAdapter
import com.mc7.aplikasigithubuser.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)

        binding.rvListUserGithub.layoutManager = LinearLayoutManager(this)
        val listAdapter = GitHubUserListAdapter{ item ->
            val intentToDetailActivity =
                Intent(this, DetailGitHubUserActivity::class.java)
            intentToDetailActivity.putExtra("key_login", item.login)
            startActivity(intentToDetailActivity)
        }
        binding.rvListUserGithub.adapter = listAdapter

        mainViewModel.isFilledUser.observe(this) { items ->
            listAdapter.submitList(items)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.isError.observe(this) {
            showError(it)
        }

        binding.svUserGithub.setupWithSearchBar(binding.sbUserGithub)
        binding.svUserGithub.editText
            .setOnEditorActionListener { searchText, _, _ ->
                binding.sbUserGithub.text = binding.svUserGithub.text
                binding.svUserGithub.hide()

                val userSearch = searchText.text.toString().trim()

                mainViewModel.getGitHubUserList(userSearch)
                false
            }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(isError: String?) {
        binding.tvErrorMessage.visibility = if (isError != null) View.VISIBLE else View.GONE

        binding.tvErrorMessage.text = isError
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.settings -> {
                val intentToSettingsActivity =
                    Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(intentToSettingsActivity)
            }

            R.id.favorite -> {
                val intentToFavoriteActivity =
                    Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(intentToFavoriteActivity)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
