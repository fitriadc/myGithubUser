package com.example.githubuser.ui

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.ListUserAdapter
import com.example.githubuser.viewModel.MainViewModel
import android.app.SearchManager
import android.content.Intent
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.githubuser.data.local.SettingPreferences
import com.example.githubuser.data.remote.response.UserItems
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.viewModel.SettingViewModel
import com.example.githubuser.viewModel.SettingViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "setting")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // Set up RecyclerView
        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager

        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)
        mainViewModel.listUser.observe(this) { listUser -> setListUserData(listUser) }
        mainViewModel.isLoading.observe(this) { showLoading(it) }

        // pengecekan agar ketika setelah login langsung temanya sesuai
        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun setListUserData(listUser: List<UserItems>) {
        val adapter = ListUserAdapter(listUser)
        binding.rvUser.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // https://developer.android.com/develop/ui/views/search/search-dialog
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = (
                menu?.findItem(R.id.search)?.actionView as SearchView).apply {
            // Assumes current activity is the searchable activity.
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(false) // Don't iconify the widget. Expand it by default.
        }
        searchView.queryHint = resources.getString(R.string.searchbar_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    mainViewModel.getGithubUsers(query) }
                searchView.clearFocus()
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // https://www.dicoding.com/academies/14/tutorials/1114
        if (item.itemId == R.id.to_setting) {
            val intent = Intent(this@MainActivity, SettingActivity::class.java)
            startActivity(intent)
        } else if (item.itemId == R.id.to_favorite) {
            val intent = Intent(this@MainActivity, FavoriteUserActivity::class.java)
            startActivity(intent)}
        return super.onOptionsItemSelected(item)
    }


}