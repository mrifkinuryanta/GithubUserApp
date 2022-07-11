package com.dev.divig.consumerapp.view

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dev.divig.consumerapp.R
import com.dev.divig.consumerapp.adapter.DetailPagerAdapter
import com.dev.divig.consumerapp.db.DatabaseContract.UsersColumns.Companion.CONTENT_URI
import com.dev.divig.consumerapp.models.UsersFavorite
import com.dev.divig.consumerapp.provider.UsersFavoriteWidget
import com.dev.divig.consumerapp.utils.showToast
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    private lateinit var uriWithId: Uri

    private var usersFavorite: UsersFavorite? = null
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        usersFavorite = intent.getParcelableExtra(EXTRA_FAVORITE)

        setDataUsersFavFromDatabase()
        isFavorite = true
        setStatusFavorite(isFavorite)


        btn_fav.setOnClickListener {
            if (isFavorite) {
                isFavorite = false

                uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + usersFavorite?.username)
                contentResolver.delete(uriWithId, null, null)

                setStatusFavorite(isFavorite)
                onBackPressed()
                updateWidget()
                this.showToast(getString(R.string.remove_favorite))
            }
        }
    }

    private fun setDataUsersFavFromDatabase() {
        val usersFav = intent.getParcelableExtra(EXTRA_FAVORITE) as UsersFavorite
        Glide.with(this)
            .load(usersFav.avatar)
            .placeholder(R.mipmap.ic_default_img_placeholder)
            .error(R.mipmap.ic_default_img_placeholder)
            .apply(RequestOptions().override(imgWidth, imgHeight))
            .into(img_detailUser)

        setActionBarTitle(usersFav.username.toString())
        setViewPager(usersFav.username.toString())

        tv_detailRepository.text = usersFav.repository
        tv_detailFollowers.text = usersFav.followers
        tv_detailFollowing.text = usersFav.following

        if (usersFav.name == mNull && usersFav.location == mNull && usersFav.company == mNull) {
            tv_detailName.text = default
            tv_detailLocation.text = default
            tv_detailCompany.text = default
        } else {
            tv_detailName.text = usersFav.name
            tv_detailLocation.text = usersFav.location
            tv_detailCompany.text = usersFav.company
        }
    }

    private fun setActionBarTitle(title: String) {
        if (supportActionBar != null) {
            supportActionBar?.title = title
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setViewPager(username: String) {
        val detailPagerAdapter = DetailPagerAdapter(this, supportFragmentManager)
        detailPagerAdapter.username = username
        view_pager.adapter = detailPagerAdapter
        tabs.setupWithViewPager(view_pager)
    }

    private fun updateWidget() {
        val widgetManager = AppWidgetManager.getInstance(this)
        val widgetIds =
            widgetManager.getAppWidgetIds(ComponentName(this, UsersFavoriteWidget::class.java))
        widgetManager.notifyAppWidgetViewDataChanged(widgetIds, R.id.stack_view)
    }


    private fun setStatusFavorite(isFavorite: Boolean) {
        if (isFavorite) {
            Glide.with(this)
                .load(R.drawable.ic_favorite_filled_24dp)
                .into(btn_fav)
        } else {
            Glide.with(this)
                .load(R.drawable.ic_favorite_outlined_24dp)
                .into(btn_fav)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_FAVORITE = "extra_favorite"
        const val EXTRA_POSITION = "extra_position"
        const val default = "-"
        const val mNull = "null"
        const val imgWidth = 125
        const val imgHeight = 125
    }
}