package com.example.githubuser.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.local.entity.FavoriteUser
import com.example.githubuser.data.remote.FavoriteUserRepository

class FavoriteUserViewModel(private val favoriteUserRepository: FavoriteUserRepository) : ViewModel()  {

    private val mFavoriteUserRepository = favoriteUserRepository
    fun insert(username: String, avatarUrl: String) {
        val favoriteUser = FavoriteUser(username, avatarUrl)
        mFavoriteUserRepository.insert(favoriteUser)
    }

    fun delete(note: FavoriteUser) {
        mFavoriteUserRepository.delete(note)
    }

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> {
       return mFavoriteUserRepository.getFavoriteUserByUsername(username)
    }

    fun getFavoriteUser(): LiveData<List<FavoriteUser>> = mFavoriteUserRepository.getAllFavoriteUser()
}