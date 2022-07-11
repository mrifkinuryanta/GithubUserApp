package com.dev.divig.githubuserapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dev.divig.githubuserapp.R
import com.dev.divig.githubuserapp.models.User
import com.dev.divig.githubuserapp.view.DetailActivity.Companion.imgHeight
import com.dev.divig.githubuserapp.view.DetailActivity.Companion.imgWidth
import kotlinx.android.synthetic.main.item_user.view.*

class ListUserAdapter(private val users: ArrayList<User>) :
    RecyclerView.Adapter<ListUserAdapter.UserHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setDataUser(items: ArrayList<User>) {
        users.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        return UserHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        )
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.bind(users[position])
    }

    inner class UserHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(user: User) {
            with(itemView) {
                tv_username.text = user.username
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .placeholder(R.mipmap.ic_default_img_placeholder)
                    .error(R.mipmap.ic_default_img_placeholder)
                    .apply(RequestOptions().override(imgWidth, imgHeight))
                    .into(img_user)
                cl_user.setOnClickListener {
                    onItemClickCallback?.onItemClicked(user)
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}