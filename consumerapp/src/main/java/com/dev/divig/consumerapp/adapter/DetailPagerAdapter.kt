package com.dev.divig.consumerapp.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dev.divig.consumerapp.R
import com.dev.divig.consumerapp.view.fragment.FollowersFragment
import com.dev.divig.consumerapp.view.fragment.FollowingFragment

class DetailPagerAdapter(private val mContext: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var username: String? = null

    private val tabTitle = intArrayOf(R.string.tv_followers, R.string.tv_following)

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowersFragment.newInstance(username)
            1 -> fragment = FollowingFragment.newInstance(username)
        }
        return fragment as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(tabTitle[position])
    }

    override fun getCount(): Int {
        return 2
    }
}