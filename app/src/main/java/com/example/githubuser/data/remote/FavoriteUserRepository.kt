package com.example.githubuser.data.remote

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubuser.data.local.entity.FavoriteUser
import com.example.githubuser.data.local.room.FavoriteUserDao
import com.example.githubuser.data.remote.retrofit.ApiService
import com.example.mygithubusers.utils.AppExecutors
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(
    private val apiService: ApiService,
    private val favoriteUserDao: FavoriteUserDao,
    private val appExecutors: AppExecutors
) {

    companion object {
        @Volatile
        private var instance: FavoriteUserRepository? = null
        fun getInstance(
            apiService: ApiService,
            favoriteUserDao: FavoriteUserDao,
            appExecutors: AppExecutors
        ): FavoriteUserRepository =
            instance ?: synchronized(this) {
                instance ?: FavoriteUserRepository(apiService, favoriteUserDao, appExecutors)
            }.also { instance = it }
    }

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> = favoriteUserDao.getAllFavoriteUser()
    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> = favoriteUserDao.getFavoriteUserByUsername(username)

    fun insert(favoriteUser: FavoriteUser) {
        appExecutors.diskIO.execute() { favoriteUserDao.insert(favoriteUser) }
    }
    fun delete(favoriteUser: FavoriteUser) {
        appExecutors.diskIO.execute() { favoriteUserDao.delete(favoriteUser) }
    }
}