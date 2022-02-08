package com.example.dicodingsubmission2.roomDb

import android.net.Uri
import android.provider.BaseColumns
import com.example.dicodingsubmission2.roomDb.UserFavorite.Companion.TABLE_NAME

object DBContracts {
    const val AUTHORITY = "com.example.dicodingsubmission2"
    const val SCHEME = "content"

    class UserFavColumns : BaseColumns {
        companion object {
            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}