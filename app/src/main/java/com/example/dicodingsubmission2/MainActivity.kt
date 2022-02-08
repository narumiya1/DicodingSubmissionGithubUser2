package com.example.dicodingsubmission2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingsubmission2.activities.FavoriteActivity
import com.example.dicodingsubmission2.adapter.UserAdapter
import com.example.dicodingsubmission2.databinding.ActivityMainBinding
import com.example.dicodingsubmission2.models.User
import com.example.dicodingsubmission2.preferences.SettingPreferences
import com.example.dicodingsubmission2.viewModel.MainViewModel
import com.example.dicodingsubmission2.viewModel.ViewModelFactory


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private var lisData : ArrayList<User> = ArrayList()
    private lateinit var adapter: UserAdapter
    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        val pref = SettingPreferences.getInstance(dataStore)

        val mainViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            MainViewModel::class.java
        )
        mainBinding.recycleView.layoutManager = LinearLayoutManager(this)
        mainBinding.recycleView.adapter = adapter

        /*
        mainBinding.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                mainBinding.searchUser.setBackgroundColor(-7829368  )
                mainBinding.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                mainBinding.switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                mainBinding.searchUser.setBackgroundColor(-3355444)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                mainBinding.searchUser.setBackgroundColor(-256)
                mainBinding.searchUser.solidColor

                switchTheme.isChecked = false
            }
        }

        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)
        */

        mainViewModel.getThemeSettings().observe(this,
            { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    mainBinding.switchTheme.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    mainBinding.switchTheme.isChecked = false
                }
            })

        mainBinding.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.saveThemeSetting(isChecked)
        }



        mainBinding.searchUser.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            val mHandler = Handler(Looper.getMainLooper())
            override fun onQueryTextChange(newText: String?): Boolean {
                mHandler.removeCallbacksAndMessages(null)
                mHandler.postDelayed({
                    if (!newText.isNullOrBlank()) {
                        showLoading(true)
                        mainViewModel.setUserList(newText)
                    }else{
                        adapter.clearData()
                    }
                }, 750)
                return true
            }
        })

        mainViewModel.getListUsers().observe(this, { listUser->
            if (listUser!=null){
                adapter.setData(listUser)
            }else{
                Toast. makeText(applicationContext,"user no available ",Toast. LENGTH_LONG)
            }
            showLoading(false)
        })

        mainViewModel.errorStatus.observe(this, {status->
            if (status != null){
                Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun showLoading(state: Boolean) {
        if (state) {
            mainBinding.progressBar.visibility = View.VISIBLE
        } else {
            mainBinding.progressBar.visibility = View.GONE
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.app_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.change_ln -> startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            R.id.favorite -> startActivity(Intent(this, FavoriteActivity::class.java ))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        mainBinding.searchUser.clearFocus()
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        mainBinding.searchUser.clearFocus()
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, R.string.double_back_pressed_to_exit, Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

}