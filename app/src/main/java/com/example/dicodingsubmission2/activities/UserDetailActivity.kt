package com.example.dicodingsubmission2.activities

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.dicodingsubmission2.R
import com.example.dicodingsubmission2.adapter.UserDetailPagerAdapter
import com.example.dicodingsubmission2.databinding.ActivityUserDetailBinding
import com.example.dicodingsubmission2.models.User
import com.example.dicodingsubmission2.roomDb.DBContracts.UserFavColumns.Companion.CONTENT_URI
import com.example.dicodingsubmission2.roomDb.UserFavorite
import com.example.dicodingsubmission2.roomDb.UserFavorite.Companion.COLUMN_AVATAR_URL
import com.example.dicodingsubmission2.roomDb.UserFavorite.Companion.COLUMN_HTML_URL
import com.example.dicodingsubmission2.roomDb.UserFavorite.Companion.COLUMN_ID
import com.example.dicodingsubmission2.roomDb.UserFavorite.Companion.COLUMN_LOGIN
import com.example.dicodingsubmission2.viewModel.DetailUserViewModel
import kotlin.concurrent.thread

class UserDetailActivity : AppCompatActivity() {

    private lateinit var detailBinding: ActivityUserDetailBinding
    private lateinit var userDetailUserViewModel: DetailUserViewModel
    private var isFavorite: Boolean = false
    private lateinit var userDetail: UserFavorite
    private lateinit var uriWithId: Uri



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

        detailBinding.fabFavorite.setOnClickListener {
            thread {
                if (!isFavorite) {
                    val values = ContentValues()
                    values.put(COLUMN_ID, userDetail.id)
                    values.put(COLUMN_LOGIN, userDetail.login)
                    values.put(COLUMN_AVATAR_URL, userDetail.avatar_url)
                    values.put(COLUMN_HTML_URL, userDetail.html_url)
                    contentResolver.insert(uriWithId, values)
                    
                } else {
                    contentResolver.delete(uriWithId, null, null)
                }
                val cursor = contentResolver.query(uriWithId, null, null, null, null)
                if (cursor != null) {
                    if (cursor.count > 0) {
                        favoriteStatus(true)
                    } else {
                        favoriteStatus(false)
                    }
                    cursor.close()
                }
            }
        }

    }
    private fun favoriteStatus(favorite: Boolean) {
        isFavorite = favorite
        if (favorite) {
            detailBinding.fabFavorite.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.ic_baseline_star_rate_24)
            )
        } else {
            detailBinding.fabFavorite.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.ic_baseline_star_border_24)
            )
        }
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

                /*
                detailBinding.tvName.text = checkNullOrEmptyString(detailUser.name)
                detailBinding.tvLocation.text = checkNullOrEmptyString(detailUser.location)
                detailBinding.tvCompany.text = checkNullOrEmptyString(detailUser.company)
                detailBinding.tvRepository.text = checkNullOrEmptyString(detailUser.public_repos.toString())
                detailBinding.tvFollowersCount.text = checkNullOrEmptyString(detailUser.followers.toString())
                detailBinding.tvFollowingCount.text = checkNullOrEmptyString(detailUser.following.toString())
                 */
                detailBinding.apply {
                    tvName.text=checkNullOrEmptyString(detailUser.name)
                    tvLocation.text = checkNullOrEmptyString(detailUser.location)
                    tvCompany.text = checkNullOrEmptyString(detailUser.company)
                    tvRepository.text = checkNullOrEmptyString(detailUser.public_repos.toString())
                    tvFollowersCount.text = checkNullOrEmptyString(detailUser.followers.toString())
                    tvFollowingCount.text = checkNullOrEmptyString(detailUser.following.toString())
                }

                userDetail = UserFavorite(
                    id = detailUser.id,
                    login = detailUser.login,
                    avatar_url = detailUser.avatarUrl,
                    html_url = detailUser.htmlUrl
                )
                thread {
                    uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + detailUser.id)
                    val cursor = contentResolver.query(uriWithId, null, null, null, null)
                    if (cursor != null) {
                        if (cursor.count > 0) {
                            favoriteStatus(true)
                        }
                        cursor.close()
                    }
                }
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

    companion object {
        const val DETAIL_USER = "detail_user"
    }
}