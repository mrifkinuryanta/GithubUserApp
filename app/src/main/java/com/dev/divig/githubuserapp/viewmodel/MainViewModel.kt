package com.dev.divig.githubuserapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dev.divig.githubuserapp.BuildConfig
import com.dev.divig.githubuserapp.models.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainViewModel : ViewModel() {

    val listDetailUsers = MutableLiveData<ArrayList<User>>()
    val getErrorMessage = MutableLiveData<String>()

    fun setSearchUser(id: String, errorMsg: String) {
        val client = AsyncHttpClient()
        client.addHeader("Authorization", BuildConfig.api_key)
        client.addHeader("User-Agent", "request")
        client.get(BuildConfig.search_url + id, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val responseObject = JSONObject(result)
                    val items = responseObject.getJSONArray("items")

                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val username = item.getString("login")
                        setUserList(username)
                    }

                    if (items.length() == 0) {
                        getErrorMessage.postValue(errorMsg)
                    }
                } catch (e: Exception) {
                    getErrorMessage.postValue(e.message)
                    Log.d(TAG, e.message.toString())
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

    fun setUserList(username: String) {
        val listDetailUser = ArrayList<User>()

        val client = AsyncHttpClient()
        client.addHeader("Authorization", BuildConfig.api_key)
        client.addHeader("User-Agent", "request")
        client.get(BuildConfig.users_url + username, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val responseObject = JSONObject(result)

                    val id = responseObject.getString("login")
                    val name = responseObject.getString("name").toString()
                    val avatar = responseObject.getString("avatar_url")
                    val location = responseObject.getString("location").toString()
                    val repository = responseObject.getInt("public_repos").toString()
                    val followers = responseObject.getInt("followers").toString()
                    val following = responseObject.getInt("following").toString()
                    val company = responseObject.getString("company").toString()

                    val user =
                        User(id, name, avatar, location, repository, followers, following, company)
                    user.username = id
                    user.name = name
                    user.avatar = avatar
                    user.location = location
                    user.repository = repository
                    user.followers = followers
                    user.following = following
                    user.company = company

                    listDetailUser.add(user)
                    listDetailUsers.postValue(listDetailUser)

                } catch (e: Exception) {
                    Log.d(TAG, e.message.toString())
                    getErrorMessage.postValue(e.message)
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

    fun getDetailUser(): LiveData<ArrayList<User>> {
        return listDetailUsers
    }

    fun getError(): LiveData<String> {
        return getErrorMessage
    }

    companion object {
        val TAG = MainViewModel::class.java.simpleName
    }
}