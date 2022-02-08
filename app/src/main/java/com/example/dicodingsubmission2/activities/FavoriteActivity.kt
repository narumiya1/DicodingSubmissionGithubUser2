package com.example.dicodingsubmission2.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dicodingsubmission2.R
import com.example.dicodingsubmission2.adapter.UserAdapter
import com.example.dicodingsubmission2.databinding.ActivityFavoriteBinding
import com.example.dicodingsubmission2.databinding.ActivityMainBinding
import com.example.dicodingsubmission2.models.User
import com.example.dicodingsubmission2.roomDb.DBContracts.UserFavColumns.Companion.CONTENT_URI
import com.example.dicodingsubmission2.roomDb.UserFavoriteMappingHelper
import kotlin.concurrent.thread

class FavoriteActivity : AppCompatActivity() {
    private lateinit var adapter: UserAdapter
    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.app_detail_fav)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        binding.rvFavorite.layoutManager= LinearLayoutManager(this)
        binding.rvFavorite.adapter = adapter

        setData()
    }

    private fun setData() {
        thread {
            val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
            val listFavUsers = UserFavoriteMappingHelper.mapCursorToArrayList(cursor)
            runOnUiThread {
                if (listFavUsers.isNotEmpty()) {
                    val list = arrayListOf<User>()
                    for (i in listFavUsers.indices) {
                        val userFav = listFavUsers[i]
                        val user = User()
                        user.id = userFav.id
                        user.login = userFav.login
                        user.avatar_url = userFav.avatar_url
                        user.html_url = userFav.html_url
                        list.add(user)
                    }
                    binding.llFavoriteBG.visibility = View.GONE
                    adapter.setData(list)
                } else {
                    binding.llFavoriteBG.visibility = View.VISIBLE
                    adapter.clearData()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setData()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }
}
