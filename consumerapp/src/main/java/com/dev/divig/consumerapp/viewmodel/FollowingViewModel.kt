package com.dev.divig.consumerapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dev.divig.consumerapp.BuildConfig
import com.dev.divig.consumerapp.models.Following
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowingViewModel : ViewModel() {

    val listFollowing = MutableLiveData<ArrayList<Following>>()
    val getErrorMessage = MutableLiveData<String>()

    fun setListFollowing(id: String?) {
        val mListFollowing = ArrayList<Following>()

        val client = AsyncHttpClient()
        client.addHeader("Authorization", BuildConfig.api_key)
        client.addHeader("User-Agent", "request")
        client.get(
            BuildConfig.following_url + id + followingUrl,
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

                            val following = Following(username, avatar)
                            following.username = username
                            following.avatar = avatar

                            mListFollowing.add(following)
                        }
                        listFollowing.postValue(mListFollowing)
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
                    Log.d(TAG, error.message.toString())
                }
            })
    }

    fun getFollowingUser(): LiveData<ArrayList<Following>> {
        return listFollowing
    }

    fun getError(): LiveData<String> {
        return getErrorMessage
    }

    companion object {
        const val followingUrl = "/following"
        val TAG = FollowingViewModel::class.java.simpleName
    }
}