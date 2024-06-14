package com.example.githubuser.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.databinding.ActivityDetailUserBinding
import com.example.githubuser.viewModel.DetailViewModel
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuser.adapter.SectionsPagerAdapter
import com.example.githubuser.data.remote.response.UserItems
import com.example.githubuser.viewModel.FavoriteUserViewModel
import com.example.githubuser.viewModel.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var binding : ActivityDetailUserBinding
    private lateinit var user : UserItems
    private lateinit var favoriteUserViewModel: FavoriteUserViewModel

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf( R.string.tab_text_1, R.string.tab_text_2)
        const val EXTRA_USER = "extra_user"
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_USER, UserItems::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_USER)
        } !!
       detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        detailViewModel = ViewModelProvider(this, factory).get(DetailViewModel::class.java)
        favoriteUserViewModel = ViewModelProvider(this, factory).get(FavoriteUserViewModel::class.java)
        favoriteUserViewModel.getFavoriteUserByUsername(user!!.login).observe(this@DetailUserActivity) { favoriteUser ->
            if (favoriteUser == null) {
                val btn: Button = findViewById(R.id.btn_fav)
                btn.setText("Add to favorites")
                btn.setOnClickListener {
                    Toast.makeText(this, "The user has successfully added to favorites", Toast.LENGTH_SHORT).show()
                    favoriteUserViewModel.insert(user.login, user.avatarUrl)
                }
            } else {
                val btn: Button = findViewById(R.id.btn_fav)
                btn.setText("Remove from favorites")
                btn.setOnClickListener {
                    Toast.makeText(this, "The user has successfully removed from favorites", Toast.LENGTH_SHORT).show()
                    favoriteUserViewModel.delete(favoriteUser)
                }
            }
        }

        binding.tvUsername.text = user.login
        detailViewModel.setUsername(user.login)
        detailViewModel.getDetailUser(user.login)

        // untuk fragment
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = user.login
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        // diletak dibawah agar data detail tetap ada lengkap meski follower / following 0, jadi saat pindah tab tetep terjaga datanya
        detailViewModel.isDetailLoading.observe(this) { showLoading(it) }
        detailViewModel.isDetailLoading.observe(this) { showLoading(it) }
        detailViewModel.name.observe(this) { name -> setName(name) }
        detailViewModel.nFollowers.observe(this) { n -> setNFollowers(n) }
        detailViewModel.nFollowing.observe(this) { n -> setNFollowing(n) }
        detailViewModel.urlAvatar.observe(this) { url -> setAvatar(url) }
        detailViewModel.company.observe(this) { company -> setCompany(company) }
        detailViewModel.location.observe(this) { location -> setLocation(location) }
        detailViewModel.htmlUrl.observe(this) { htmlUrl -> setHtmlUrl(htmlUrl) }

        supportActionBar?.elevation = 0f

    }

    private fun setAvatar(url: String?) {
        Glide.with(this@DetailUserActivity).load(url).into(binding.ivAvatar)
    }

    private fun setName(name: String?) {
        binding.ivName.text = name
    }

    private fun setCompany(company: String?) {
        binding.tvCompany.text = company
    }

    private fun setLocation(location: String?) {
        binding.tvLocation.text = location
    }

    private fun setHtmlUrl(htmlUrl: String?) {
        binding.tvUrl.text = htmlUrl
    }

    private fun setNFollowers(n: Int?) {
        binding.tvNFollowers.text = "${n.toString()} Followers"
    }

    private fun setNFollowing(n: Any) {
        binding.tvNFollowing.text = "${n.toString()} Following"
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}