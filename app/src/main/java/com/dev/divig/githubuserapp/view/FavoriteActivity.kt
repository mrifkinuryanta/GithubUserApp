package com.dev.divig.githubuserapp.view

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev.divig.githubuserapp.R
import com.dev.divig.githubuserapp.adapter.FavoriteAdapter
import com.dev.divig.githubuserapp.db.DatabaseContract.UsersColumns.Companion.CONTENT_URI
import com.dev.divig.githubuserapp.helper.MappingHelper
import com.dev.divig.githubuserapp.models.UsersFavorite
import com.dev.divig.githubuserapp.utils.showSnackBar
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        supportActionBar?.title = getString(R.string.favorite_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setRecyclerView()

        val handleThread = HandlerThread(DATA_OBSERVER)
        handleThread.start()
        val handler = Handler(handleThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadUsersFavAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadUsersFavAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<UsersFavorite>(EXTRA_STATE)
            if (list != null) {
                adapter.listFavorite = list
            }
        }
    }

    private fun loadUsersFavAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBar_favorite.visibility = View.VISIBLE
            val deferredUsersFav = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progressBar_favorite.visibility = View.INVISIBLE
            val users = deferredUsersFav.await()
            if (users.size > 0) {
                adapter.listFavorite = users
            } else {
                adapter.listFavorite = ArrayList()
                rv_for_user_favorite.visibility = View.INVISIBLE
                showSnackBar(rv_for_user_favorite, getString(R.string.empty_list))
            }
        }
    }

    private fun setRecyclerView() {
        rv_for_user_favorite.layoutManager = LinearLayoutManager(this)
        rv_for_user_favorite.setHasFixedSize(true)
        adapter = FavoriteAdapter(this)
        rv_for_user_favorite.adapter = adapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavorite)
    }

    override fun onResume() {
        super.onResume()
        loadUsersFavAsync()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        private const val EXTRA_STATE = "extra_state"
        private const val DATA_OBSERVER = "DataObserver"
    }
}