package com.dev.divig.githubuserapp.adapter

import android.content.Context
import android.content.Intent
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dev.divig.githubuserapp.R
import com.dev.divig.githubuserapp.db.DatabaseContract.UsersColumns.Companion.CONTENT_URI
import com.dev.divig.githubuserapp.helper.MappingHelper
import com.dev.divig.githubuserapp.models.UsersFavorite
import com.dev.divig.githubuserapp.provider.UsersFavoriteWidget

internal class StackRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory {
    private var mWidgetItems = ArrayList<UsersFavorite>()

    override fun onCreate() {
    }

    override fun onDataSetChanged() {
        val identityToken = Binder.clearCallingIdentity()

        val cursor = mContext.contentResolver.query(CONTENT_URI, null, null, null, null)
        val users = MappingHelper.mapCursorToArrayList(cursor)
        if (users.size > 0) {
            mWidgetItems.clear()
            mWidgetItems.addAll(users)
        } else {
            mWidgetItems = ArrayList()
        }

        Binder.restoreCallingIdentity(identityToken)

    }

    override fun onDestroy() {
    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)

        val options =
            RequestOptions().placeholder(R.mipmap.ic_default_img_placeholder)
                .error(R.mipmap.ic_default_img_placeholder)
                .fitCenter()
                .circleCrop()
        val imgBitmap = Glide.with(mContext)
            .asBitmap()
            .load(mWidgetItems[position].avatar)
            .apply(options)
            .submit(img_width, img_height)
            .get()
        val username: CharSequence = mWidgetItems[position].username.toString()

        rv.setImageViewBitmap(R.id.imageView, imgBitmap)
        rv.setTextViewText(R.id.tv_widget_username, username)

        val extras = bundleOf(
            UsersFavoriteWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false

    companion object {
        const val img_width = 300
        const val img_height = 300
    }
}