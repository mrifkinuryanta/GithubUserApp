package com.dev.divig.githubuserapp.view

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dev.divig.githubuserapp.R
import com.dev.divig.githubuserapp.adapter.DetailPagerAdapter
import com.dev.divig.githubuserapp.db.DatabaseContract.UsersColumns.Companion.AVATAR
import com.dev.divig.githubuserapp.db.DatabaseContract.UsersColumns.Companion.COMPANY
import com.dev.divig.githubuserapp.db.DatabaseContract.UsersColumns.Companion.CONTENT_URI
import com.dev.divig.githubuserapp.db.DatabaseContract.UsersColumns.Companion.FOLLOWERS
import com.dev.divig.githubuserapp.db.DatabaseContract.UsersColumns.Companion.FOLLOWING
import com.dev.divig.githubuserapp.db.DatabaseContract.UsersColumns.Companion.LOCATION
import com.dev.divig.githubuserapp.db.DatabaseContract.UsersColumns.Companion.NAME
import com.dev.divig.githubuserapp.db.DatabaseContract.UsersColumns.Companion.REPOSITORY
import com.dev.divig.githubuserapp.db.DatabaseContract.UsersColumns.Companion.USERNAME
import com.dev.divig.githubuserapp.helper.MappingHelper
import com.dev.divig.githubuserapp.models.User
import com.dev.divig.githubuserapp.models.UsersFavorite
import com.dev.divig.githubuserapp.provider.UsersFavoriteWidget
import com.dev.divig.githubuserapp.utils.showToast
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    private lateinit var uriWithId: Uri
    private lateinit var users: User

    private var usersFavorite: UsersFavorite? = null
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        usersFavorite = intent.getParcelableExtra(EXTRA_FAVORITE)
        if (usersFavorite != null) {
            setDataUsersFavFromDatabase()
            isFavorite = true
            setStatusFavorite(isFavorite)
        } else {
            users = intent.getParcelableExtra(KEY_USERS) as User
            loadUsersFav(users.username)
            setDataForUser()
        }

        btn_fav.setOnClickListener {
            if (isFavorite) {
                isFavorite = false
                if (usersFavorite != null) {
                    uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + usersFavorite?.username)
                    contentResolver.delete(uriWithId, null, null)
                    setStatusFavorite(isFavorite)
                    onBackPressed()
                } else {
                    contentResolver.delete(uriWithId, null, null)
                    setStatusFavorite(isFavorite)
                }
                updateWidget()
                this.showToast(getString(R.string.remove_favorite))
            } else {
                val values = ContentValues()
                values.put(USERNAME, users.username)
                values.put(NAME, users.name)
                values.put(AVATAR, users.avatar)
                values.put(LOCATION, users.location)
                values.put(REPOSITORY, users.repository)
                values.put(FOLLOWERS, users.followers)
                values.put(FOLLOWING, users.following)
                values.put(COMPANY, users.company)

                contentResolver.insert(CONTENT_URI, values)

                isFavorite = !isFavorite
                setStatusFavorite(isFavorite)
                updateWidget()
                this.showToast(getString(R.string.added_favorite))
            }
        }
    }

    private fun setDataForUser() {
        Glide.with(this)
            .load(users.avatar)
            .placeholder(R.mipmap.ic_default_img_placeholder)
            .error(R.mipmap.ic_default_img_placeholder)
            .apply(RequestOptions().override(imgWidth, imgHeight))
            .into(img_detailUser)

        setActionBarTitle(users.username)
        setViewPager(users.username)

        tv_detailRepository.text = users.repository
        tv_detailFollowers.text = users.followers
        tv_detailFollowing.text = users.following

        if (users.name == mNull && users.location == mNull && users.company == mNull) {
            tv_detailName.text = default
            tv_detailLocation.text = default
            tv_detailCompany.text = default
        } else {
            tv_detailName.text = users.name
            tv_detailLocation.text = users.location
            tv_detailCompany.text = users.company
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

    private fun loadUsersFav(username: String) {
        uriWithId = Uri.parse("$CONTENT_URI/$username")
        val cursor = contentResolver.query(uriWithId, null, null, null, null)
        val users = MappingHelper.mapCursorToArrayList(cursor)
        if (users.size > 0) {
            isFavorite = true
            setStatusFavorite(isFavorite)
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
        const val KEY_USERS = "key_users"
        const val EXTRA_FAVORITE = "extra_favorite"
        const val EXTRA_POSITION = "extra_position"
        const val default = "-"
        const val mNull = "null"
        const val imgWidth = 125
        const val imgHeight = 125
    }
}