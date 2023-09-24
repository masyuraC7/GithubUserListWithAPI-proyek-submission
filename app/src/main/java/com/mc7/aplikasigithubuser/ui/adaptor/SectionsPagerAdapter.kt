package com.mc7.aplikasigithubuser.ui.adaptor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mc7.aplikasigithubuser.ui.FollowFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    private var username: String = ""

    fun setUsername(userLog: String){
        username = userLog
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowFragment()

        fragment.arguments = Bundle().apply {
            putInt(FollowFragment.ARG_POSITION, position + 1)
            putString(FollowFragment.ARG_USERNAME, username)
        }

        return fragment
    }

}