package com.dicoding.mygithubuserapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mygithubuserapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ListUserAdapter
    private lateinit var mainModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showRecyclerList()
        mainModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
        mainModel.getUser().observe(this, Observer { user->
            if (user != null){
                showUser(user)
                binding.progressBar.visibility = View.INVISIBLE
            }
        })


    }

    private fun showRecyclerList(){
        adapter = ListUserAdapter()
        binding.homeList.layoutManager = LinearLayoutManager(this)
        binding.homeList.adapter = adapter
        adapter.notifyDataSetChanged()
        binding.homeList.setHasFixedSize(true)

        adapter.setOnItemClickCallback(object :ListUserAdapter.OnItemClickCallback{
            override fun onItemClicked(user: User) {
                Toast.makeText(this@MainActivity, "kamu memilih : ${user.username}",Toast.LENGTH_SHORT).show()
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USER, user)
                startActivity(intent)
            }

        })
    }

    private fun showUser(listUser: ArrayList<User>){
        adapter.setData(listUser)
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = "username"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                binding.progressBar.visibility = View.VISIBLE
                mainModel.setUser(query)
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                binding.progressBar.visibility = View.VISIBLE
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(choose: MenuItem): Boolean {
        when(choose.itemId){
            R.id.favorit ->{
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
            }

            R.id.setting ->{
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(choose)
    }

}