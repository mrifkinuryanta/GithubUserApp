package com.dev.divig.githubuserapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev.divig.githubuserapp.R
import com.dev.divig.githubuserapp.adapter.ListFollowingAdapter
import com.dev.divig.githubuserapp.models.Following
import com.dev.divig.githubuserapp.viewmodel.FollowingViewModel
import kotlinx.android.synthetic.main.fragment_following.*


class FollowingFragment : Fragment() {
    private val following = ArrayList<Following>()

    private lateinit var adapter: ListFollowingAdapter
    private lateinit var followingViewModel: FollowingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar_following.visibility = View.VISIBLE

        setRecyclerView()

        val username = arguments?.getString(USERNAME)
        followingViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowingViewModel::class.java)
        followingViewModel.setListFollowing(username)

        followingViewModel.getFollowingUser().observe(viewLifecycleOwner, { itemFollowing ->
            if (itemFollowing != null) {
                adapter.setFollowingUser(itemFollowing)
                progressBar_following.visibility = View.INVISIBLE
            }
        })

        followingViewModel.getError().observe(viewLifecycleOwner, { errorMessage ->
            if (errorMessage != null) {
                if (following.isEmpty()) {
                    Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
                    progressBar_following.visibility = View.INVISIBLE
                }
            }
        })
    }

    private fun setRecyclerView() {
        recycler_view_for_following.layoutManager = LinearLayoutManager(activity)
        adapter = ListFollowingAdapter(following)
        adapter.notifyDataSetChanged()
        recycler_view_for_following.adapter = adapter
    }

    companion object {
        private const val USERNAME = "username"

        fun newInstance(username: String?): FollowingFragment {
            val fragment =
                FollowingFragment()
            val bundle = Bundle()
            bundle.putString(USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }
}