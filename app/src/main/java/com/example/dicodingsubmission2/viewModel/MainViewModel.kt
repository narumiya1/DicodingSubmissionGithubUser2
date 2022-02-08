package com.example.dicodingsubmission2.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.example.dicodingsubmission2.BuildConfig
import com.example.dicodingsubmission2.models.User
import com.example.dicodingsubmission2.network.RetrofitClient
import com.example.dicodingsubmission2.preferences.SettingPreferences
import com.google.gson.JsonElement
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences) : ViewModel() {
    val listUser = MutableLiveData<ArrayList<User>>()

    private val baseURL = "https://api.github.com/"
//    private val authToken = "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"
    private val authToken = BuildConfig.GITHUB_TOKEN
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
                                /*
                                if (list.size==0){
                                    errorStatus.value = "Null"
                                }
                                 */
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
    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

}