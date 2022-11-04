package com.vasukammedia.kawsar.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vasukammedia.kawsar.fragments.audio
import com.vasukammedia.kawsar.fragments.video

class ViewPagerAdapter(fragmentManager: FragmentManager,lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
     return   when(position){
            0->{
                audio()
            }
            1->{
                video()
            }
            else->{
                Fragment()
            }

        }
    }
}