package com.example.livesaver.home.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.livesaver.home.presentation.fragments.ImagesFragment
import com.example.livesaver.home.presentation.fragments.SavedFragment
import com.example.livesaver.home.presentation.fragments.SettingsFragment
import com.example.livesaver.home.presentation.fragments.VideosFragment

class FragmentPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
):FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return 4
    }
    override fun createFragment(position: Int): Fragment {
        when(position) {
            0-> return SettingsFragment()
            1-> return ImagesFragment()
            2-> return VideosFragment()
            else -> return SavedFragment()
        }
    }
}