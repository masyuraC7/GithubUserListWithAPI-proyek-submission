package com.mc7.aplikasigithubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.mc7.aplikasigithubuser.R
import com.mc7.aplikasigithubuser.data.Result
import com.mc7.aplikasigithubuser.data.remote.response.ItemsItem
import com.mc7.aplikasigithubuser.databinding.ActivityMainBinding
import com.mc7.aplikasigithubuser.ui.adaptor.GitHubUserListAdapter
import com.mc7.aplikasigithubuser.ui.viewmodel.MainViewModel
import com.mc7.aplikasigithubuser.ui.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val settingViewModel: SettingsViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingViewModel.getThemeSettings().observe(this){ isDarkMode: Boolean ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        setSupportActionBar(binding.topAppBar)

        binding.rvListUserGithub.setHasFixedSize(true)

        mainViewModel.isLoading.observe(this){
            showLoading(it)
        }

        mainViewModel.isError.observe(this){
            showError(it)
        }

        if (mainViewModel.isFilledGitHubUserList()){
            mainViewModel.getSavedGitHubUserList()?.let { showRvListGitHubUser(it) }
        }else{
            setGitHubUserList("arip")
        }

        binding.svUserGithub.setupWithSearchBar(binding.sbUserGithub)
        binding.svUserGithub.editText
            .setOnEditorActionListener{ searchText, _, _ ->
                binding.sbUserGithub.text = binding.svUserGithub.text
                binding.svUserGithub.hide()

                val userSearch = searchText.text.toString().trim()

                setGitHubUserList(userSearch)
                false
            }
    }

    private fun setGitHubUserList(userSearch: String){
        mainViewModel.getGitHubUserList(userSearch)
            .observe(this){ result ->
                if (result != null){
                    when (result) {
                        is Result.Loading -> {
                            mainViewModel.isLoading(true)
                        }
                        is Result.Success -> {
                            if (result.data.isEmpty()){
                                val errorMsg = "Data user tidak ditemukan"
                                mainViewModel.isError(errorMsg)
                            }else{
                                mainViewModel.isError("")
                            }
                            mainViewModel.isLoading(false)

                            val newsData = ArrayList<ItemsItem>()
                            newsData.addAll(result.data)
                            mainViewModel.saveGitHubUserList(newsData)

                            showRvListGitHubUser(newsData)
                        }
                        is Result.Error -> {
                            mainViewModel.isLoading(false)
                            val msgError = "Terjadi kesalahan" + result.error
                            mainViewModel.isError(msgError)
                        }
                    }
                }else{
                    val errorMsg = "Gagal memuat API"
                    mainViewModel.isError(errorMsg)
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

    private fun showRvListGitHubUser(listGitHubUser: ArrayList<ItemsItem>) {
        binding.rvListUserGithub.layoutManager = LinearLayoutManager(this)
        val listAdapter = GitHubUserListAdapter(listGitHubUser)
        binding.rvListUserGithub.adapter = listAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId){
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
