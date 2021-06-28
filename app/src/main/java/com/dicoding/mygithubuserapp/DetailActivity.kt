package com.dicoding.mygithubuserapp

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.mygithubuserapp.database.DatabaseContract.FavoriteCol.Companion.PROFILE
import com.dicoding.mygithubuserapp.database.DatabaseContract.FavoriteCol.Companion.USERNAME
import com.dicoding.mygithubuserapp.database.DatabaseContract.FavoriteCol.Companion.USER_ID
import com.dicoding.mygithubuserapp.database.FavoriteHelper
import com.dicoding.mygithubuserapp.databinding.ActivityDetailBinding
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var helper: FavoriteHelper
    private var index: Int = 0
    private var user: User? = null
    private var status = false

    companion object {
        const val EXTRA_USER = "extra_user"
        const val FAVORTIE = "favorite"
        const val INDEX = "index"
        const val ADD_REQUEST = 100
        const val ADD_RESULT = 101
        const val DELETE_REQUEST = 300
        const val DELETE_RESULT = 301
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        helper = FavoriteHelper.getInstance(applicationContext)
        helper.open()
        setContentView(binding.root)
        user = intent.getParcelableExtra(EXTRA_USER)
        val bundle = Bundle()
        bundle.putString(EXTRA_USER, this.user.toString())
        setIntentUser(user)
        changeFav()
        fav_add.setOnClickListener {

            val username = user?.username
            val id = user?.id
            val avatar = user?.avatar

            val intent = Intent()
            intent.putExtra(FAVORTIE, user)
            intent.putExtra(INDEX, index)

            val values = ContentValues().apply {
                put(USERNAME, username)
                put(USER_ID, id)
                put(PROFILE, avatar)
            }

            if (status){
                val result = helper.deleteByUsername(user?.username.toString())
                if (result > 0) {
                    setResult(DELETE_RESULT, intent)
                }else{
                    Log.e("Message: ","Tidak bisa hapus favorite")
                }
            } else {
                val result = helper.insert(values)
                if (result > 0) {
                    user?.username = result.toString()
                    setResult(ADD_RESULT, intent)
                    changeIcon()
                }else{
                    Log.e("Message: ","Tidak bisa add favorite")
                }
            }
            changeFav()

        }


        val sectionsPagerAdapter = SectionPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.identity = user?.username
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)

        val actionBar = supportActionBar

        supportActionBar?.title = "Detail User"
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun changeFav() {
        val cursor = helper.queryByUsername()
        val listData = MappingHelper.mapCursorToArrayList(cursor)
        if (listData.size > 0) {
            for (data in listData) {
                if (user?.username == data.username) {
                    status = true
                    break
                } else {
                    status = false
                }
            }
        } else {
            status = false
        }
        changeIcon()
    }

    private fun changeIcon(){
        if (status){
            fav_add.setImageResource(R.drawable.ic_baseline_favorite_24)
        }else{
            fav_add.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    private fun setIntentUser(user: User?){
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            DetailViewModel::class.java)
        viewModel.setDetailUser(user?.username)
        viewModel.getDetailUser().observe(this, Observer {
            if (it != null){
                binding.nama.text = it.name
                binding.txtUsername.text = it.username
                binding.repository.text = it.repository
                binding.followerInt.text = it.follower
                binding.followingInt.text = it.following
                Glide.with(this)
                        .load(it.avatar)
                        .into(profile_img)
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}