package com.example.client.ui.projectList

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class ProjectListAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    val ARG_OBJECT = "object"

    override fun createFragment(position: Int): Fragment {
        val fragment = ProjectListFragment()
        fragment.arguments = Bundle().apply {
            putInt(ARG_OBJECT, position + 1)
        }
        return fragment
    }

    override fun getItemCount(): Int = 3

}