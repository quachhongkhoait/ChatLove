package com.cj.chatlove_app.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.cj.chatlove_app.ui.fragment.chat.ChatFragment
import com.cj.chatlove_app.ui.fragment.home.HomeFragment
import com.cj.chatlove_app.ui.fragment.notification.NotificationFragment
import com.cj.chatlove_app.ui.fragment.profile.ProfileFragment

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var mList: MutableList<Fragment> = mutableListOf()

    override fun getCount(): Int {
        return mList.size
    }

    override fun getItem(position: Int): Fragment {
        return mList[position]
    }

    fun addData() {
        mList.add(ChatFragment())
        mList.add(HomeFragment())
        mList.add(NotificationFragment())
        mList.add(ProfileFragment())
    }
}
