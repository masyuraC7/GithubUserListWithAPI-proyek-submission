package com.example.aplikasigithubuser.apiretrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    private const val baseUrl = "https://api.github.com/"

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getService() : ApiService{
        return getRetrofit().create(ApiService::class.java)
    }
}