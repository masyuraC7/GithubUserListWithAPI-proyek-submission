package com.example.aplikasigithubuser

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplikasigithubuser.databinding.ItemUserGithubBinding

class FollowFragmentAdapter(private val listFollowUser: ArrayList<ItemsItem>) :
    RecyclerView.Adapter<FollowFragmentAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemUserGithubBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        Glide.with(holder.itemView.context).load(listFollowUser[position].avatarUrl)
            .circleCrop().into(holder.binding.imgProfile)

        holder.binding.txtUsernameGithub.text = listFollowUser[position].login

        holder.binding.imgProfile.setOnClickListener{
            val intent = Intent(holder.itemView.context, DetailGitHubUserActivity::class.java)
            intent.putExtra("key_login", listFollowUser[holder.adapterPosition].login)
            holder.itemView.context.startActivity(intent)
        }

        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, DetailGitHubUserActivity::class.java)
            intent.putExtra("key_login", listFollowUser[holder.adapterPosition].login)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listFollowUser.size

    class ListViewHolder(var binding: ItemUserGithubBinding) : RecyclerView.ViewHolder(binding.root)
}
