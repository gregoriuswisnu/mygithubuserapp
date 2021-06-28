package com.dicoding.mygithubuserapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mygithubuserapp.database.FavoriteHelper
import com.dicoding.mygithubuserapp.databinding.ActivityFavoriteBinding
import com.dicoding.mygithubuserapp.provider.MyContentProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private lateinit var adapter: FavoriteAdapter
    private lateinit var helper: FavoriteHelper
    private lateinit var binding: ActivityFavoriteBinding

    companion object {
        private const val ITEM = "item"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        helper = FavoriteHelper.getInstance(applicationContext)
        adapter = FavoriteAdapter()
        helper.open()
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.favList.layoutManager = LinearLayoutManager(this)
        binding.favList.setHasFixedSize(true)

        val actionBar = supportActionBar

        supportActionBar?.title = "Favorite User"
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)



        if (savedInstanceState == null) {
            loadNotesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<User>(ITEM)
            if (list != null) {
                adapter.mData = list
            }
        }
        binding.favList.adapter = adapter

        adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: User) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_USER, user)
        startActivity(intent)
    }

    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBarFav.visibility = View.VISIBLE
            val defferedNotes = async(Dispatchers.IO) {
                val cursor = helper.queryByUsername()
                MappingHelper.mapCursorToArrayList(cursor)
            }

            binding.progressBarFav.visibility = View.INVISIBLE
            val notes = defferedNotes.await()
            if (notes.size > 0) {
                adapter.mData = notes
            } else {
                adapter.mData = ArrayList()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(ITEM, adapter.mData)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            when (requestCode) {
                DetailActivity.ADD_REQUEST -> if (resultCode == DetailActivity.ADD_RESULT) {
                    val addFavUser = data.getParcelableExtra<User>(DetailActivity.FAVORTIE)
                    if (addFavUser != null) {
                        adapter.setAddToFav(addFavUser)
                    }
                }
                DetailActivity.DELETE_REQUEST -> if (resultCode == DetailActivity.DELETE_RESULT) {
                    val index = data.getIntExtra(DetailActivity.INDEX, 0)
                    adapter.setRemoveFav(index)
                }
            }
        }
    }
}