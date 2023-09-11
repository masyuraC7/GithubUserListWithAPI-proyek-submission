package com.example.aplikasigithubuser

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasigithubuser.apiretrofit.ApiConfig
import com.example.aplikasigithubuser.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private val listGitHubUser = ArrayList<ItemsItem>()
    private lateinit var errorMessage: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding.rvListUserGithub.setHasFixedSize(true)

        listGitHubUser.addAll(viewModel.getListGitHubUser())
        errorMessage = viewModel.getErrorMessage()

        if (errorMessage == "") {
            binding.tvErrorMessage.visibility = View.INVISIBLE

            if (listGitHubUser.size <= 0) {
                getGithubUserList("arip")
            }
            else {
                binding.progressBar.visibility = View.INVISIBLE
                showRvListGitHubUser()
            }
        }else{
            binding.progressBar.visibility = View.VISIBLE
            binding.tvErrorMessage.text = errorMessage
        }

        binding.svUserGithub.setupWithSearchBar(binding.sbUserGithub)
        binding.svUserGithub.editText
            .setOnEditorActionListener{ textView, _, _ ->
                binding.sbUserGithub.text = binding.svUserGithub.text
                binding.svUserGithub.hide()
                listGitHubUser.clear()
                getGithubUserList(textView.text.toString())
                false
            }
    }

    private fun getGithubUserList(queryUser: String) {
        val list = ArrayList<ItemsItem>()

        binding.progressBar.visibility = View.VISIBLE

        ApiConfig.getService().getListUsers(queryUser).enqueue(object : Callback<GitHubResponse> {
            override fun onResponse(call: Call<GitHubResponse>, response: Response<GitHubResponse>) {
                if (response.isSuccessful) {
                    val responseUserList = response.body()

                    list.addAll(responseUserList?.items as ArrayList<ItemsItem>)

                    if (list.size <= 0) {
                        errorMessage = "Data username tidak ditemukan!!!"

                        Toast.makeText(this@MainActivity,
                            errorMessage, Toast.LENGTH_SHORT).show()

                        binding.progressBar.visibility = View.VISIBLE
                        binding.tvErrorMessage.visibility = View.VISIBLE
                        return
                    } else {
                        errorMessage = ""
                        binding.tvErrorMessage.visibility = View.INVISIBLE
                    }

                    Toast.makeText(this@MainActivity,
                        "Berhasil memuat data user", Toast.LENGTH_SHORT).show()

                    viewModel.getErrorMessage(errorMessage)
                    binding.tvErrorMessage.text = errorMessage

                    binding.progressBar.visibility = View.INVISIBLE

                    viewModel.getListGitHubUser(list)
                    listGitHubUser.addAll(list)

                    showRvListGitHubUser()
                }
            }

            override fun onFailure(call: Call<GitHubResponse>, t: Throwable) {
                binding.progressBar.visibility = View.INVISIBLE

                val errorMessage = "Gagal memuat data user, \nsilahkan coba lagi setelah beberapa saat."

                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showRvListGitHubUser() {
        binding.rvListUserGithub.layoutManager = LinearLayoutManager(this)
        val listAdapter = ListGitHubUserAdapter(listGitHubUser)
        binding.rvListUserGithub.adapter = listAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
