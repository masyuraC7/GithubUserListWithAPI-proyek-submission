package com.mc7.aplikasigithubuser.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.mc7.aplikasigithubuser.BuildConfig
import com.mc7.aplikasigithubuser.data.MyApp
import com.mc7.aplikasigithubuser.data.SettingPreferences
import com.mc7.aplikasigithubuser.data.dataStore
import com.mc7.aplikasigithubuser.data.local.room.FavUserDao
import com.mc7.aplikasigithubuser.data.local.room.FavUserDatabase
import com.mc7.aplikasigithubuser.data.remote.retrofit.ApiService
import com.mc7.aplikasigithubuser.data.repository.GitHubUserRepositoryImp
import com.mc7.aplikasigithubuser.domain.repository.GitHubUserRepository
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
    fun provideFavUserDatabase(appContext: MyApp): FavUserDatabase{
        return Room.databaseBuilder(appContext,
            FavUserDatabase::class.java, "FavoriteUser.db")
            .build()
    }

    @Provides
    fun provideFavUserDao(favUserDatabase: FavUserDatabase): FavUserDao{
        return favUserDatabase.favUserDao()
    }

    @Provides
    fun provideGitHubUserRepository(apiService: ApiService, favUserDao: FavUserDao):
            GitHubUserRepository{
        return GitHubUserRepositoryImp(apiService, favUserDao)
    }

    @Provides
    fun provideDataStorePreferences(appContext: MyApp): DataStore<Preferences> {
        return appContext.dataStore
    }

    @Provides
    fun provideSettingsPreferences(dataStore: DataStore<Preferences>): SettingPreferences {
        return SettingPreferences(dataStore)
    }
}