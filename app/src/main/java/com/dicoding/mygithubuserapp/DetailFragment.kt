package com.dicoding.mygithubuserapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_detail.*
import org.json.JSONArray


class DetailFragment : Fragment(){

    private lateinit var adapter: ListUserAdapter
    private val userList = ArrayList<User>()
    companion object {
        private val EXTRA_USERNAME = "username"
        fun newInstance(username: String?): DetailFragment {
            val fragment = DetailFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ListUserAdapter()
        adapter.notifyDataSetChanged()
        following_list.layoutManager = LinearLayoutManager(activity)
        following_list.adapter = adapter
        val data = arguments?.getString(EXTRA_USERNAME)
        setFollowing(data)
    }

    private fun setFollowing(username: String?) {
        progressBar_Following.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/following"
        client.addHeader("Authorization", "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")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray
            ) {
                progressBar_Following.visibility = View.INVISIBLE
                try {
                    val result = String(responseBody)
                    val items = JSONArray(result)
                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val listItem = User()
                        listItem.username = item.getString("login")
                        listItem.avatar = item.getString("avatar_url")
                        listItem.id = item.getString("id")
                        userList.add(listItem)
                    }
                    adapter.setData(userList)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<out Header?>, responseBody: ByteArray, error: Throwable
            ) {
                progressBar_Following.visibility = View.INVISIBLE
                Log.d("onFailure", error.message.toString())
            }
        })
    }

}
