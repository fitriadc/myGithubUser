package com.example.githubuser.di

import android.content.Context
import com.example.githubuser.data.local.room.FavoriteUserDatabase
import com.example.githubuser.data.remote.FavoriteUserRepository
import com.example.githubuser.data.remote.retrofit.ApiConfig
import com.example.mygithubusers.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): FavoriteUserRepository {
        val apiService = ApiConfig.getApiService()
        val database = FavoriteUserDatabase.getDatabase(context)
        val dao = database.favoriteUserDao()
        val appExecutors = AppExecutors()
        return FavoriteUserRepository.getInstance(apiService, dao, appExecutors)
    }
}