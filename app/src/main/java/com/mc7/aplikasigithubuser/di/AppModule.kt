package com.mc7.aplikasigithubuser.di

import android.content.Context
import com.mc7.aplikasigithubuser.BuildConfig
import com.mc7.aplikasigithubuser.data.MyApp
import com.mc7.aplikasigithubuser.data.remote.retrofit.ApiService
import com.mc7.aplikasigithubuser.data.repository.DetailUserRepositoryImp
import com.mc7.aplikasigithubuser.data.repository.FollowRepositoryImp
import com.mc7.aplikasigithubuser.data.repository.UserListRepositoryImp
import com.mc7.aplikasigithubuser.domain.repository.DetailUserRepository
import com.mc7.aplikasigithubuser.domain.repository.FollowRepository
import com.mc7.aplikasigithubuser.domain.repository.UserListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideMyApplication(@ApplicationContext appContext: Context): MyApp {
        return appContext as MyApp
    }

    @Provides
    fun provideApiService(): ApiService {
        val loggingInterceptor = Interceptor { chain ->
            val req = chain.request()
            val requestHeaders = req.newBuilder()
                .addHeader("Authorization", BuildConfig.githubToken)
                .build()
            chain.proceed(requestHeaders)
        }

        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.githubAPI)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }

    @Provides
    fun provideUserListRepository(apiService: ApiService): UserListRepository {
        return UserListRepositoryImp(apiService)
    }

    @Provides
    fun provideDetailUserRepository(apiService: ApiService): DetailUserRepository {
        return DetailUserRepositoryImp(apiService)
    }

    @Provides
    fun provideFollowRepository(apiService: ApiService): FollowRepository {
        return FollowRepositoryImp(apiService)
    }
}