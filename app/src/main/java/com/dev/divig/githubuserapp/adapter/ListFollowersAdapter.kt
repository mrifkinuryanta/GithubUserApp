package com.dev.divig.githubuserapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dev.divig.githubuserapp.R
import com.dev.divig.githubuserapp.models.Followers
import com.dev.divig.githubuserapp.view.DetailActivity.Companion.imgHeight
import com.dev.divig.githubuserapp.view.DetailActivity.Companion.imgWidth
import kotlinx.android.synthetic.main.item_user.view.*

class ListFollowersAdapter(private val followers: ArrayList<Followers>) :
    RecyclerView.Adapter<ListFollowersAdapter.FollowersHolder>() {

    fun setFollowerUser(items: ArrayList<Followers>) {
        followers.clear()
        followers.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowersHolder {
        return FollowersHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        )
    }

    override fun getItemCount(): Int = followers.size

    override fun onBindViewHolder(holder: FollowersHolder, position: Int) {
        holder.bind(followers[position])
    }

    class FollowersHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(follower: Followers) {
            with(itemView) {
                tv_username.text = follower.username
                Glide.with(itemView.context)
                    .load(follower.avatar)
                    .placeholder(R.mipmap.ic_default_img_placeholder)
                    .error(R.mipmap.ic_default_img_placeholder)
                    .apply(RequestOptions().override(imgWidth, imgHeight))
                    .into(img_user)
            }
        }
    }
}