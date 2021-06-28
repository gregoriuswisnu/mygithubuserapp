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

class MainViewModel: ViewModel() {
    val listUser = MutableLiveData<ArrayList<User>>()
    val apiKey = "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"

    fun setUser(user: String){
        val url = "https://api.github.com/search/users?q=${user}"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $apiKey")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray
            ) {
                val result = String(responseBody)
                val listItems = ArrayList<User>()
                try {
                    val responseObjects = JSONObject(result)
                    val list = responseObjects.getJSONArray("items")
                    for (i in 0 until list.length()){
                        val user = User()
                        val item = list.getJSONObject(i)
                        user.username = item.getString("login")
                        user.avatar = item.getString("avatar_url")
                        user.id = item.getInt("id").toString()
                        listItems.add(user)
                    }
                    listUser.postValue(listItems)
                }catch (e: Exception){
                    Log.d("EXCEPTION",e.message.toString())
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable) {
                Log.d("onFailure", error.message.toString())
            }

        })
    }
    fun getUser(): LiveData<ArrayList<User>> {
        return listUser
    }

}