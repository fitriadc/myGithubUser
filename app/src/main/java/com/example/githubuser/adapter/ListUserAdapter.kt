package com.example.githubuser.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.data.remote.response.UserItems
import com.example.githubuser.databinding.ItemUserBinding
import com.example.githubuser.ui.DetailUserActivity

class ListUserAdapter(private val listUser: List<UserItems>) : RecyclerView.Adapter<ListUserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvUsername.text = listUser[position].login
        Glide.with(holder.itemView.context)
            .load(listUser[position].avatarUrl)
            .into(holder.binding.ivProfilePic)
        holder.itemView.setOnClickListener() {
            val intent = Intent(holder.itemView.context, DetailUserActivity::class.java)
            intent.putExtra("extra_user", listUser[holder.adapterPosition])
            holder.itemView.context.startActivity(intent)
        }
    }

    class ViewHolder(var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

}