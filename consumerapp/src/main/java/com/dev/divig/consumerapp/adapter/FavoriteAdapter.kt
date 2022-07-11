package com.dev.divig.consumerapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dev.divig.consumerapp.R
import com.dev.divig.consumerapp.models.UsersFavorite
import com.dev.divig.consumerapp.utils.CustomOnItemClickListener
import com.dev.divig.consumerapp.view.DetailActivity
import kotlinx.android.synthetic.main.item_user.view.*

class FavoriteAdapter(private val activity: Activity) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    var listFavorite = ArrayList<UsersFavorite>()
        set(listFavorite) {
            if (listFavorite.size > 0) {
                this.listFavorite.clear()
            }
            this.listFavorite.addAll(listFavorite)

            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }

    override fun getItemCount(): Int = this.listFavorite.size

    inner class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(usersFavorite: UsersFavorite) {
            with(itemView) {
                tv_username.text = usersFavorite.username
                Glide.with(itemView.context)
                    .load(usersFavorite.avatar)
                    .placeholder(R.mipmap.ic_default_img_placeholder)
                    .error(R.mipmap.ic_default_img_placeholder)
                    .apply(
                        RequestOptions().override(
                            DetailActivity.imgWidth,
                            DetailActivity.imgHeight
                        )
                    )
                    .into(img_user)
                cl_user.setOnClickListener(
                    CustomOnItemClickListener(adapterPosition,
                        object : CustomOnItemClickListener.OnItemClickCallback {
                            override fun onItemClicked(v: View, position: Int) {
                                val mIntent = Intent(activity, DetailActivity::class.java)
                                mIntent.putExtra(DetailActivity.EXTRA_FAVORITE, usersFavorite)
                                mIntent.putExtra(DetailActivity.EXTRA_POSITION, position)
                                activity.startActivity(mIntent)
                            }
                        })
                )
            }
        }
    }
}