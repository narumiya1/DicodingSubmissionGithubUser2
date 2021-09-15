package com.example.dicodingsubmission2.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingsubmission2.models.User
import com.example.dicodingsubmission2.network.RetrofitClient
import com.google.gson.JsonElement
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class MainViewModel : ViewModel() {
    val listUser = MutableLiveData<ArrayList<User>>()

    private val baseURL = "https://api.github.com/"
    private val authToken = "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"
    var errorStatus = MutableLiveData<String?>()

    fun setUserList(username : String ){
        RetrofitClient(baseURL).instanceUserList.getListUsers(authToken, username)
            .enqueue(
                object : retrofit2.Callback<JsonElement> {
                    override fun onResponse(
                        call: Call<JsonElement>,
                        response: Response<JsonElement>
                    ) {
                        if (response.isSuccessful) {
                            try {
                                val jsonObject = JSONObject(response.body().toString())
                                val jsonArrayItem: JSONArray = jsonObject.getJSONArray("items")

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
                                listUser.postValue(list)
                            } catch (e: JSONException) {
                                Log.d("Exception", e.message.toString())
                                errorStatus.value = "Exception - " + e.message.toString()
                            }
                        } else {
                            Log.d("response.isFailed", "Response Not Successful")
                            errorStatus.value = "response.isFailed - Response Not Successful"
                        }
                    }

                    override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                        Log.d("onFailure", t.message.toString())
                        errorStatus.value = "onFailure - " + t.message.toString()
                    }
                })

    }
    fun getListUsers(): LiveData<ArrayList<User>> {
        return listUser
    }
}