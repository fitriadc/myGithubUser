package com.example.githubuser.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.adapter.ListUserAdapter
import com.example.githubuser.data.remote.response.UserItems
import com.example.githubuser.databinding.ActivityFavoriteUserBinding
import com.example.githubuser.viewModel.FavoriteUserViewModel
import com.example.githubuser.viewModel.ViewModelFactory

class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUserBinding
    private lateinit var favoriteUserViewModel: FavoriteUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        favoriteUserViewModel = ViewModelProvider(this, factory).get(FavoriteUserViewModel::class.java)
        favoriteUserViewModel.getFavoriteUser().observe(this) { users ->
            val items = arrayListOf<UserItems>()
            users.map {
                val item = UserItems(it.username, it.avatarUrl!!)
                items.add(item)
            }
            //adapter.submitList(items)
            binding.rvUser.adapter = ListUserAdapter(items)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
    }
}