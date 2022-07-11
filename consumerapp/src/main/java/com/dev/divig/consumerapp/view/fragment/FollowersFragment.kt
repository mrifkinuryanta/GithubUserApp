package com.dev.divig.consumerapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev.divig.consumerapp.R
import com.dev.divig.consumerapp.adapter.ListFollowersAdapter
import com.dev.divig.consumerapp.models.Followers
import com.dev.divig.consumerapp.viewmodel.FollowersViewModel
import kotlinx.android.synthetic.main.fragment_followers.*

class FollowersFragment : Fragment() {
    private val followers = ArrayList<Followers>()

    private lateinit var followersViewModel: FollowersViewModel
    private lateinit var adapter: ListFollowersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar_followers.visibility = View.VISIBLE

        setRecyclerView()

        val username = arguments?.getString(USERNAME)
        followersViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowersViewModel::class.java)
        followersViewModel.setListFollowers(username)

        followersViewModel.getFollowersUser().observe(viewLifecycleOwner, { itemFollower ->
            if (itemFollower != null) {
                adapter.setFollowerUser(itemFollower)
                progressBar_followers.visibility = View.INVISIBLE
            }
        })

        followersViewModel.getError().observe(viewLifecycleOwner, { errorMessage ->
            if (errorMessage != null) {
                if (followers.isEmpty()) {
                    Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
                    progressBar_followers.visibility = View.INVISIBLE
                }
            }
        })

    }

    private fun setRecyclerView() {
        recycler_view_for_followers.layoutManager = LinearLayoutManager(activity)
        adapter = ListFollowersAdapter(followers)
        adapter.notifyDataSetChanged()
        recycler_view_for_followers.adapter = adapter
    }

    companion object {
        private const val USERNAME = "username"

        fun newInstance(username: String?): FollowersFragment {
            val fragment =
                FollowersFragment()
            val bundle = Bundle()
            bundle.putString(USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }
}