package com.dev.divig.githubuserapp.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dev.divig.githubuserapp.R
import com.dev.divig.githubuserapp.adapter.ListUserAdapter
import com.dev.divig.githubuserapp.models.User
import com.dev.divig.githubuserapp.utils.showToast
import com.dev.divig.githubuserapp.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val users = ArrayList<User>()

    private lateinit var adapter: ListUserAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setRecyclerView()

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java
        )

        mainViewModel.getDetailUser().observe(this, { itemUser ->
            if (itemUser != null) {
                adapter.setDataUser(itemUser)
                progressBar.visibility = View.INVISIBLE
                img_searching.visibility = View.GONE
            } else {
                progressBar.visibility = View.INVISIBLE
            }
        })

        mainViewModel.getError().observe(this, { errorMessage ->
            if (errorMessage != null) {
                if (users.isEmpty()) {
                    this.showToast(errorMessage)
                    progressBar.visibility = View.INVISIBLE
                }
            }
        })

        Glide.with(this)
            .load(R.drawable.img_searching)
            .into(img_searching)
    }

    private fun setRecyclerView() {
        recycler_view_for_user.layoutManager = LinearLayoutManager(this)
        adapter = ListUserAdapter(users)
        adapter.notifyDataSetChanged()
        recycler_view_for_user.adapter = adapter

        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val mIntent = Intent(this@MainActivity, DetailActivity::class.java)
                mIntent.putExtra(DetailActivity.KEY_USERS, data)
                startActivity(mIntent)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        val errorMsg = getString(R.string.user_unavailable)

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty()) {
                    return true
                } else {
                    users.clear()
                    mainViewModel.setSearchUser(query, errorMsg)
                    progressBar.visibility = View.VISIBLE

                    val methodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    methodManager.hideSoftInputFromWindow(searchView.windowToken, 0)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite -> {
                val mIntent = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(mIntent)
                true
            }
            R.id.setting -> {
                val mIntent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(mIntent)
                true
            }
            else -> true
        }
    }
}
