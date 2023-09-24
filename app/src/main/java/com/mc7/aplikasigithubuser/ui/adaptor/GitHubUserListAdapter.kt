package com.mc7.aplikasigithubuser.ui.adaptor

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mc7.aplikasigithubuser.data.remote.response.ItemsItem
import com.mc7.aplikasigithubuser.databinding.ItemUserGithubBinding
import com.mc7.aplikasigithubuser.ui.DetailGitHubUserActivity

class GitHubUserListAdapter(private val listGitHubUser: ArrayList<ItemsItem>) :
    RecyclerView.Adapter<GitHubUserListAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemUserGithubBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        Glide.with(holder.itemView.context).load(listGitHubUser[position].avatarUrl)
        .circleCrop().into(holder.binding.imgProfile)

        holder.binding.txtUsernameGithub.text = listGitHubUser[position].login

        holder.binding.imgProfile.setOnClickListener{
            val intent = Intent(holder.itemView.context, DetailGitHubUserActivity::class.java)
            intent.putExtra("key_login", listGitHubUser[holder.adapterPosition].login)
            holder.itemView.context.startActivity(intent)
        }

        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, DetailGitHubUserActivity::class.java)
            intent.putExtra("key_login", listGitHubUser[holder.adapterPosition].login)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listGitHubUser.size

    class ListViewHolder(var binding: ItemUserGithubBinding) : RecyclerView.ViewHolder(binding.root)
}
