package com.dicoding.mygithubuserapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class DetailViewModel: ViewModel() {

    val detailUser = MutableLiveData<User>()
    val apiKey = "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"

    fun setDetailUser(username: String?) {

        val url = "https://api.github.com/users/$username"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $apiKey")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseObjects = JSONObject(result)
                    val detail = User()
                    detail.avatar = responseObjects.getString("avatar_url")
                    detail.username = responseObjects.getString("login")
                    detail.name = responseObjects.getString("name")
                    detail.repository = responseObjects.getInt("public_repos").toString()
                    detail.follower = responseObjects.getInt("followers").toString()
                    detail.following = responseObjects.getInt("following").toString()
                    detailUser.postValue(detail)
                } catch (e: Exception) {
                    Log.d("onSuccess", e.message.toString())
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    fun getDetailUser(): LiveData<User> {
        return detailUser
    }

}

