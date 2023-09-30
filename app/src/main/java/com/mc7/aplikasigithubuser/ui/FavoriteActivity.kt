package com.mc7.aplikasigithubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mc7.aplikasigithubuser.R
import com.mc7.aplikasigithubuser.databinding.ActivityFavoriteBinding
import com.mc7.aplikasigithubuser.ui.adapter.FavoriteAdapter
import com.mc7.aplikasigithubuser.ui.viewmodel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private val viewModel: FavoriteViewModel by viewModels()

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        if (result.resultCode == DetailGitHubUserActivity.RESULT_CODE && result.data != null){
            viewModel.getAllFavUser()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.isError.observe(this) {
            showError(it)
        }

        binding.rvListFavoriteUser.layoutManager = LinearLayoutManager(this)

        val listAdapter = FavoriteAdapter(
            onClick = { item ->
                val intentToDetailActivity =
                    Intent(this@FavoriteActivity, DetailGitHubUserActivity::class.java)
                intentToDetailActivity.putExtra("key_login", item.username)
                resultLauncher.launch(intentToDetailActivity)
//                startActivity(intentToDetailActivity)
            },
            onDelete = { item ->
                viewModel.delete(item)
            })
        binding.rvListFavoriteUser.adapter = listAdapter

        viewModel.getAllFavUser()

        viewModel.isFilledFavorite.observe(this) {
            listAdapter.submitList(it)
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
        if (menu != null) {
            menu.findItem(R.id.favorite).isVisible = false
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.settings -> {
                val intentToSettingsActivity =
                    Intent(this@FavoriteActivity, SettingsActivity::class.java)
                startActivity(intentToSettingsActivity)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}