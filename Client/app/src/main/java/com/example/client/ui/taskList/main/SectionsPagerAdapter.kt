package com.example.client.ui.taskList.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.client.ui.taskList.FirstFragment
import com.example.client.ui.taskList.SecondFragment
import com.example.client.ui.taskList.ThirdFragment


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return when (position) {
            0 -> {
                FirstFragment()
            }
            1 -> SecondFragment()
            else -> {
                return ThirdFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "TASKS"
            1 -> "PICTURES"
            else -> {
                return "FILES"
            }
        }
    }

    override fun getCount(): Int {
        // Show 3 total pages.
        return 3
    }
}