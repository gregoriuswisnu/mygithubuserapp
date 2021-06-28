package com.dicoding.mygithubuserapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_follower.*
import org.json.JSONArray


class Follower : Fragment(){

    private lateinit var adapter: ListUserAdapter
    private val listFollower = ArrayList<User>()
    companion object {
        private val ARG_USERNAME ="username"
        fun newInstance(username: String?): Follower {
            val fragment = Follower()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_follower, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ListUserAdapter()
        adapter.notifyDataSetChanged()

        follower_list.layoutManager = LinearLayoutManager(activity)
        follower_list.adapter = adapter

        val username = arguments?.getString(ARG_USERNAME)
        getFollowers(username)

        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                Toast.makeText(activity, data.username, Toast.LENGTH_SHORT).show()
            }
        })

    }
    private fun getFollowers(username: String?) {
        progressBar_Follower.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/followers"
        client.addHeader("Authorization", "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")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header?>, responseBody: ByteArray
            ) {
                progressBar_Follower.visibility = View.INVISIBLE
                try {
                    val result = String(responseBody)
                    val items = JSONArray(result)
                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val userItems = User()
                        userItems.username = item.getString("login")
                        userItems.avatar = item.getString("avatar_url")
                        userItems.id = item.getString("id")
                        listFollower.add(userItems)

                    }
                    adapter.setData(listFollower)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header?>, responseBody: ByteArray, error: Throwable
            ) {
                progressBar_Follower.visibility = View.INVISIBLE
                Log.d("OnFailure", error.message.toString())
            }
        })
    }

}


