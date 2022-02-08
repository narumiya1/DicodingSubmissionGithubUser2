package com.example.dicodingsubmission2.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingsubmission2.BuildConfig
import com.example.dicodingsubmission2.models.DetailUser
import com.example.dicodingsubmission2.models.User
import com.example.dicodingsubmission2.network.RetrofitClient
import com.google.gson.JsonElement
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel : ViewModel() {
    val userDetailValue = MutableLiveData<DetailUser>()

    private val baseURL = "https://api.github.com/"
//    private val authToken = "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"
    private val authToken = BuildConfig.GITHUB_TOKEN

    val listFollowers = MutableLiveData<ArrayList<User>>()
    val listFollowing = MutableLiveData<ArrayList<User>>()

    var statusError = MutableLiveData<String?>()

    fun setUserDetail(username: String) {
        RetrofitClient(baseURL).instanceDetailUser.getDetailUser(authToken, username)
            .enqueue(
                object : Callback<JsonElement> {
                    override fun onResponse(
                        call: Call<JsonElement>,
                        response: Response<JsonElement>
                    ) {
                        if (response.isSuccessful){
                            try {
                                val dataJson = JSONObject(response.body().toString())
                                val userDetail = DetailUser()
                                userDetail.id = dataJson.getInt("id")
                                userDetail.login = dataJson.getString("login")
                                userDetail.avatarUrl = dataJson.getString("avatar_url")
                                userDetail.htmlUrl = dataJson.getString("html_url")
                                userDetail.name = dataJson.getString("name")
                                userDetail.company = dataJson.getString("company")
                                userDetail.blog = dataJson.getString("blog")
                                userDetail.location = dataJson.getString("location")
                                userDetail.email = dataJson.getString("email")
                                userDetail.public_repos = dataJson.getInt("public_repos")
                                userDetail.public_gists = dataJson.getInt("public_gists")
                                userDetail.followers = dataJson.getInt("followers")
                                userDetail.following = dataJson.getInt("following")
                                userDetail.created_at = dataJson.getString("created_at")
                                userDetail.updated_at = dataJson.getString("updated_at")

                                val folw= dataJson.getInt("followers")
                                Log.d(" "," "+folw)

                                userDetailValue.postValue(userDetail)

                            }catch (e: JSONException){
                                Log.d("Exception (Detail ViewModel)", e.message.toString())

                            }
                        }

                    }

                    override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                        Log.d("Exception (Detail ViewModel)", t.message.toString())

                    }

                }
            )
    }

    fun getDetailUser(): LiveData<DetailUser>{
        return userDetailValue
    }

    fun setFollowerUser(username: String) {
        RetrofitClient(baseURL).instanceFollowers.getFollowers(authToken, username)
            .enqueue(
                object : Callback<JsonElement> {
                    override fun onResponse(
                        call: Call<JsonElement>,
                        response: Response<JsonElement>
                    ) {
                        if (response.isSuccessful) {
                            try {
                                val jsonArrayItem = JSONArray(response.body().toString())
                                val list = arrayListOf<User>()
                                for (i in 0 until jsonArrayItem.length()) {
                                    val dataJSON = jsonArrayItem.getJSONObject(i)
                                    val user = User()
                                    user.id = dataJSON.getInt("id")
                                    user.login = dataJSON.getString("login")
                                    user.avatar_url = dataJSON.getString("avatar_url")
                                    user.html_url = dataJSON.getString("html_url")
                                    list.add(user)
                                }
                                listFollowers.postValue(list)
                            } catch (e: JSONException) {
                                Log.d("Exception (Followers)", e.message.toString())
                                statusError.value =
                                    "Exception (Followers) - " + e.message.toString()
                            }
                        } else {
                            Log.d("responseFlwers.isFailed", "Response Not Successful")
                            statusError.value = "responseFlwers.isFailed - Response Not Successful"
                        }
                    }

                    override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                        Log.d("onFailure (Followers)", t.message.toString())
                        statusError.value = "onFailure (Followers) - " + t.message.toString()
                    }
                })
    }

    fun setFollowingUser(username: String) {
        RetrofitClient(baseURL).instanceFollowing.getFollowing(authToken, username)
            .enqueue(
                object : Callback<JsonElement> {
                    override fun onResponse(
                        call: Call<JsonElement>,
                        response: Response<JsonElement>
                    ) {
                        if (response.isSuccessful) {
                            try {
                                val jsonArrayItem = JSONArray(response.body().toString())
                                val list = arrayListOf<User>()
                                for (i in 0 until jsonArrayItem.length()) {
                                    val dataJSON = jsonArrayItem.getJSONObject(i)
                                    val user = User()
                                    user.id = dataJSON.getInt("id")
                                    user.login = dataJSON.getString("login")
                                    user.avatar_url = dataJSON.getString("avatar_url")
                                    user.html_url = dataJSON.getString("html_url")
                                    list.add(user)
                                }
                                listFollowing.postValue(list)
                            } catch (e: JSONException) {
                                Log.d("Exception (Following)", e.message.toString())
                                statusError.value =
                                    "Exception (Following) - " + e.message.toString()
                            }
                        } else {
                            Log.d("responseFlwing.isFailed", "Response Not Successful")
                            statusError.value = "responseFlwing.isFailed - Response Not Successful"
                        }
                    }

                    override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                        Log.d("onFailure (Following)", t.message.toString())
                        statusError.value = "onFailure (Following) - " + t.message.toString()
                    }
                })
    }

    fun getFollowerUser(): LiveData<ArrayList<User>> {
        return listFollowers
    }

    fun getFollowingUser(): LiveData<ArrayList<User>> {
        return listFollowing
    }


}