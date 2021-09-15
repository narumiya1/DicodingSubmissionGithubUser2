package com.example.dicodingsubmission2.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.dicodingsubmission2.R
import com.example.dicodingsubmission2.adapter.UserDetailPagerAdapter
import com.example.dicodingsubmission2.databinding.ActivityUserDetailBinding
import com.example.dicodingsubmission2.models.User
import com.example.dicodingsubmission2.viewModel.DetailUserViewModel

class UserDetailActivity : AppCompatActivity() {

    private lateinit var detailBinding: ActivityUserDetailBinding
    private lateinit var userDetailUserViewModel: DetailUserViewModel

    companion object {
        const val DETAIL_USER = "detail_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        userDetailUserViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailUserViewModel::class.java)

        val userDetail = intent.getParcelableExtra<User>(DETAIL_USER) as User
        actionbar?.title = resources.getString(R.string.app_detail_user)

        Glide.with(this)
            .load(userDetail.avatar_url)
            .apply(
                RequestOptions()
                    .error(R.drawable.ic_baseline_broken_image_24)
                    .placeholder(R.drawable.ic_baseline_person_24)
            )
            .into(detailBinding.imgAvatarDetail)

        detailBinding.tvUserNameDetail.text = userDetail.login


        val detailPagerAdapter = UserDetailPagerAdapter(this,supportFragmentManager)
        detailPagerAdapter.username = userDetail.login
        detailBinding.viewPager.adapter = detailPagerAdapter
        detailBinding.tabs.setupWithViewPager(detailBinding.viewPager)


        getDetailUserData(userDetail.login)

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    private fun checkNullOrEmptyString(text: String?): String {
        return when {
            text.isNullOrBlank() -> {
                " - "
            }
            text == "null" -> {
                " - "
            }
            else -> {
                text
            }
        }
    }

    private fun getDetailUserData(login: String) {
        showLoading(true)

        userDetailUserViewModel.setUserDetail(login)

        userDetailUserViewModel.getDetailUser().observe(this, { detailUser ->

            if (detailUser != null) {

                detailBinding.tvName.text = checkNullOrEmptyString(detailUser.name)
                detailBinding.tvLocation.text = checkNullOrEmptyString(detailUser.location)
                detailBinding.tvCompany.text = checkNullOrEmptyString(detailUser.company)
                detailBinding.tvRepository.text = checkNullOrEmptyString(detailUser.public_repos.toString())
                detailBinding.tvFollowersCount.text = checkNullOrEmptyString(detailUser.followers.toString())
                detailBinding.tvFollowingCount.text = checkNullOrEmptyString(detailUser.following.toString())

                showLoading(false)
            }

        })

    }


    private fun showLoading(state: Boolean) {
        if (state) {
            detailBinding.progressBar.visibility = View.VISIBLE
        } else {
            detailBinding.progressBar.visibility = View.GONE
        }
    }
}