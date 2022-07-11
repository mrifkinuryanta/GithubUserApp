package com.dev.divig.githubuserapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dev.divig.githubuserapp.BuildConfig
import com.dev.divig.githubuserapp.models.Followers
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowersViewModel : ViewModel() {

    val listFollowers = MutableLiveData<ArrayList<Followers>>()
    val getErrorMessage = MutableLiveData<String>()

    fun setListFollowers(id: String?) {
        val listFollower = ArrayList<Followers>()

        val client = AsyncHttpClient()
        client.addHeader("Authorization", BuildConfig.api_key)
        client.addHeader("User-Agent", "request")
        client.get(
            BuildConfig.followers_url + id + followersUrl,
            object : AsyncHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray
                ) {
                    val result = String(responseBody)
                    Log.d("result", result)
                    try {
                        val responseArray = JSONArray(result)

                        for (i in 0 until responseArray.length()) {
                            val items = responseArray.getJSONObject(i)
                            val username = items.getString("login")
                            val avatar = items.getString("avatar_url")

                            val follower = Followers(username, avatar)
                            follower.username = username
                            follower.avatar = avatar

                            listFollower.add(follower)
                        }
                        listFollowers.postValue(listFollower)
                    } catch (e: Exception) {
                        Log.d("Exception", e.message.toString())
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable
                ) {
                    val errorMessage = when (statusCode) {
                        401 -> "$statusCode : Bad Request"
                        403 -> "$statusCode : Forbidden"
                        404 -> "$statusCode : Not Found"
                        else -> "$statusCode : ${error.message}"
                    }
                    getErrorMessage.postValue(errorMessage)
                    Log.d(MainViewModel.TAG, error.message.toString())
                }
            })
    }

    fun getFollowersUser(): LiveData<ArrayList<Followers>> {
        return listFollowers
    }

    fun getError(): LiveData<String> {
        return getErrorMessage
    }

    companion object {
        const val followersUrl = "/followers"
    }
}