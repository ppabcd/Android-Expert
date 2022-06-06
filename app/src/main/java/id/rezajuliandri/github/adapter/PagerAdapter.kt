package id.rezajuliandri.github.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import id.rezajuliandri.github.followingfollowers.FollowingFollowersFragment
import id.rezajuliandri.github.followingfollowers.FollowingFollowersFragment.Companion.ARG_SECTION_TYPE
import id.rezajuliandri.github.followingfollowers.FollowingFollowersFragment.Companion.ARG_USERNAME
import id.rezajuliandri.github.followingfollowers.FollowingFollowersFragment.FollType.FOLLOWERS
import id.rezajuliandri.github.followingfollowers.FollowingFollowersFragment.FollType.FOLLOWING
import id.rezajuliandri.github.repository.RepositoryFragment
import id.rezajuliandri.github.repository.RepositoryFragment.Companion.ARG_USERNAME as REPOSITORY_USERNAME_ARG

class PagerAdapter(activity: AppCompatActivity, private val username: String) :
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                fragment = RepositoryFragment()
                fragment.arguments = Bundle().apply {
                    putString(REPOSITORY_USERNAME_ARG, username)
                }
            }
            1 -> {
                fragment = FollowingFollowersFragment()
                fragment.arguments = Bundle().apply {
                    putSerializable(ARG_SECTION_TYPE, FOLLOWERS)
                    putString(ARG_USERNAME, username)
                }
            }
            2 -> {
                fragment = FollowingFollowersFragment()
                fragment.arguments = Bundle().apply {
                    putSerializable(ARG_SECTION_TYPE, FOLLOWING)
                    putString(ARG_USERNAME, username)
                }
            }
        }
        return fragment as Fragment
    }
}