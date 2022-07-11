package com.dev.divig.consumerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dev.divig.consumerapp.R
import com.dev.divig.consumerapp.models.Following
import com.dev.divig.consumerapp.view.DetailActivity.Companion.imgHeight
import com.dev.divig.consumerapp.view.DetailActivity.Companion.imgWidth
import kotlinx.android.synthetic.main.item_user.view.*

class ListFollowingAdapter(private val following: ArrayList<Following>) :
    RecyclerView.Adapter<ListFollowingAdapter.FollowingHolder>() {

    fun setFollowingUser(items: ArrayList<Following>) {
        following.clear()
        following.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FollowingHolder {
        return FollowingHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        )
    }

    override fun getItemCount(): Int = following.size

    override fun onBindViewHolder(holder: FollowingHolder, position: Int) {
        holder.bind(following[position])
    }

    class FollowingHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(following: Following) {
            with(itemView) {
                tv_username.text = following.username
                Glide.with(itemView.context)
                    .load(following.avatar)
                    .placeholder(R.mipmap.ic_default_img_placeholder)
                    .error(R.mipmap.ic_default_img_placeholder)
                    .apply(RequestOptions().override(imgWidth, imgHeight))
                    .into(img_user)
            }
        }
    }
}