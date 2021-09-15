package com.example.dicodingsubmission2.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.dicodingsubmission2.R
import com.example.dicodingsubmission2.fragment.FollowerFragment

class UserDetailPagerAdapter (private val mContext: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int {
        return 2
    }

    lateinit var username: String
    private val tabTitles = intArrayOf(R.string.follower, R.string.following)

    override fun getPageTitle(position: Int): CharSequence {
        return mContext.resources.getString(tabTitles[position])
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowerFragment.newInstance(0, username)
            1 -> fragment = FollowerFragment.newInstance(1, username)
        }
        return fragment as Fragment
    }
}