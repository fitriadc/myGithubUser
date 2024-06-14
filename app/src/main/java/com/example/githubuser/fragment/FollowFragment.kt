package com.example.githubuser.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuser.R
import com.example.githubuser.adapter.ListUserAdapter
import com.example.githubuser.data.remote.response.UserItems
import com.example.githubuser.viewModel.DetailViewModel

class FollowFragment : Fragment() {
    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProvider(this)[DetailViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follow, container, false)
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var position = 0
        var username = ""

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME).toString()
        }

        if (username != "") {
            viewModel.getFollowers(username)
            viewModel.getFollowing(username)
        }

        if (position == 1){
            val rv: RecyclerView = view.findViewById(R.id.rvFollow)
            rv.layoutManager = LinearLayoutManager(requireActivity())
            rv.setHasFixedSize(true)
            viewModel.listFollowers.observe(viewLifecycleOwner) {
                listFollower -> setListFollower(listFollower, rv)
                Log.e("list follower", listFollower.take(5).toString())
            }
            viewModel.isFollowersLoading.observe(viewLifecycleOwner) { showLoading(it, view) }
        } else {
            val rv: RecyclerView = view.findViewById(R.id.rvFollow)
            rv.layoutManager = LinearLayoutManager(requireActivity())
            rv.setHasFixedSize(true)
            viewModel.listFollowing.observe(viewLifecycleOwner) { listFollowing -> setListFollowing(listFollowing, rv)
                Log.e("list following", listFollowing.take(5).toString())}
            viewModel.isFollowingLoading.observe(viewLifecycleOwner) { showLoading(it, view) }
        }
    }

    private fun showLoading(isLoading: Boolean, view: View) {
        view.findViewById<ProgressBar>(R.id.progressBar).visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setListFollowing(list: List<UserItems>, rv: RecyclerView){
        val adapter = ListUserAdapter(list)
        rv.adapter = adapter
    }

    private fun setListFollower(list: List<UserItems>, rv: RecyclerView){
        val adapter = ListUserAdapter(list)
        rv.adapter = adapter
    }


}