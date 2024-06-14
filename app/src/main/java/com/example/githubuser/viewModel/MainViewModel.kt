package com.example.githubuser.viewModel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.remote.response.UserItems
import com.example.githubuser.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    companion object {
        private const val USERNAME = "fitriadc"
    }

    private val _listUser = MutableLiveData<List<UserItems>>()
    val listUser: LiveData<List<UserItems>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getGithubUsers(USERNAME)
    }

    fun getGithubUsers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearchUser(username)

        client.enqueue(object : Callback<com.example.githubuser.data.remote.response.Response> {
            override fun onResponse(
                call: Call<com.example.githubuser.data.remote.response.Response>,
                response: Response<com.example.githubuser.data.remote.response.Response>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUser.value = response.body()?.items
                }
            }
            override fun onFailure(call: Call<com.example.githubuser.data.remote.response.Response>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
}