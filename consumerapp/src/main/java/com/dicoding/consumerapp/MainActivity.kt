package com.dicoding.consumerapp

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.consumerapp.DatabaseContract.FavoriteCol.Companion.CONTENT_URI
import com.dicoding.consumerapp.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: FavoriteAdapter
    private lateinit var binding: ActivityMainBinding

    companion object{
        private const val ITEM = "item"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = FavoriteAdapter()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.homeList.layoutManager = LinearLayoutManager(this)
        binding.homeList.setHasFixedSize(true)

        val actionBar = supportActionBar

        supportActionBar?.title = "Consumer App"
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)

        val handlerThread = HandlerThread("MyContentProvider")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadNotesAsync()
            }
        }
        contentResolver?.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null){
            loadNotesAsync()
        }else{
            val list = savedInstanceState.getParcelableArrayList<User>(ITEM)
            if (list != null) {
                adapter.mData = list
            }
        }
        binding.homeList.adapter = adapter

    }

    private fun loadNotesAsync() {
        GlobalScope.launch (Dispatchers.Main){
            binding.progressBar.visibility = View.VISIBLE
            val defferedNotes = async (Dispatchers.IO){
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            binding.progressBar.visibility = View.INVISIBLE
            val notes = defferedNotes.await()
            if (notes.size >0){
                adapter.mData = notes
            }else{
                adapter.mData = ArrayList()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}