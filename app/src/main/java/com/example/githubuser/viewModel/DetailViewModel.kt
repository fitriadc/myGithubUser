package com.example.githubuser.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.remote.response.UserDetailResponse
import com.example.githubuser.data.remote.response.UserItems
import com.example.githubuser.data.remote.retrofit.ApiConfig
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel(){
    private var _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _nFollowers = MutableLiveData<Int>()
    val nFollowers: LiveData<Int> = _nFollowers

    private val _nFollowing = MutableLiveData<Int>()
    val nFollowing: LiveData<Int> = _nFollowing

    private val _listFollowers = MutableLiveData<List<UserItems>>()
    val listFollowers: LiveData<List<UserItems>> = _listFollowers

    private val _listFollowing = MutableLiveData<List<UserItems>>()
    val listFollowing: LiveData<List<UserItems>> = _listFollowing

    private val _urlAvatar = MutableLiveData<String>()
    val urlAvatar: LiveData<String> = _urlAvatar

    private val _location = MutableLiveData<String>()
    val location: LiveData<String> = _location

    private val _company = MutableLiveData<String>()
    val company: LiveData<String> = _company

    private val _htmlUrl = MutableLiveData<String>()
    val htmlUrl: LiveData<String> = _htmlUrl

    private val _isFollowersLoading = MutableLiveData<Boolean>()
    val isFollowersLoading: LiveData<Boolean> = _isFollowersLoading

    private val _isFollowingLoading = MutableLiveData<Boolean>()
    val isFollowingLoading: LiveData<Boolean> = _isFollowingLoading

    private val _isDetailLoading = MutableLiveData<Boolean>()
    val isDetailLoading: LiveData<Boolean> = _isDetailLoading

    fun getDetailUser(username: String) {
        _isDetailLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                if (response.isSuccessful) {
                    _isDetailLoading.value = false
                    _name.value = response.body()?.name
                    _nFollowers.value = response.body()?.followers
                    _nFollowing.value = response.body()?.following
                    _urlAvatar.value = response.body()?.avatarUrl
                    _company.value = response.body()?.company
                    _location.value = response.body()?.location
                    _htmlUrl.value = response.body()?.htmlUrl
                }
            }
            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                _isDetailLoading.value = false
            }
        })
    }

    fun setUsername(username: String) {
        _username.value = username
    }

    fun getFollowers(username: String) {
        _isFollowersLoading.value = true
        val client = ApiConfig.getApiService().getListUserFollower(username)
        client.enqueue(object : Callback<List<UserItems>> {
            override fun onResponse(
                call: Call<List<UserItems>>,
                response: Response<List<UserItems>>
            ) {
                _isFollowersLoading.value = false
                _listFollowers.value = response.body()
            }
            override fun onFailure(call: Call<List<UserItems>>, t: Throwable) {
                _isFollowersLoading.value = false
            }
        })
    }

    fun getFollowing(username: String) {
        _isFollowingLoading.value = true
        val client = ApiConfig.getApiService().getListUserFollowing(username)
        client.enqueue(object : Callback<List<UserItems>> {
            override fun onResponse(
                call: Call<List<UserItems>>,
                response: Response<List<UserItems>>
            ) {
                _isFollowingLoading.value = false
                _listFollowing.value = response.body()
            }
            override fun onFailure(call: Call<List<UserItems>>, t: Throwable) {
                _isFollowingLoading.value = false
            }
        })
    }
}