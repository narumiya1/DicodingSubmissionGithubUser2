package com.example.dicodingsubmission2.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class User(
    var id: Int = 0,
    var login: String = "",
    var avatar_url: String = "",
    var html_url: String = ""
): Parcelable