package com.stenleone.stanleysfilm.ui.adapter.viewPager

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.stenleone.stanleysfilm.ui.fragment.YouTubeVideoFragment
import com.stenleone.stanleysfilm.ui.model.VideosResultUI

class YouTubeTrailersAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    val itemList = ArrayList<VideosResultUI>()

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun createFragment(position: Int): Fragment {
       return YouTubeVideoFragment.instance(itemList[position].key, itemList[position].name)
    }

}