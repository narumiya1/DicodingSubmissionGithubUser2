package com.example.dicodingsubmission2.roomDb

import android.database.Cursor
import com.example.dicodingsubmission2.roomDb.UserFavorite.Companion.COLUMN_AVATAR_URL
import com.example.dicodingsubmission2.roomDb.UserFavorite.Companion.COLUMN_HTML_URL
import com.example.dicodingsubmission2.roomDb.UserFavorite.Companion.COLUMN_ID
import com.example.dicodingsubmission2.roomDb.UserFavorite.Companion.COLUMN_LOGIN
import java.util.ArrayList

object UserFavoriteMappingHelper {
    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<UserFavorite> {
        val listUserFav = ArrayList<UserFavorite>()
        cursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(COLUMN_ID))
                val login = getString(getColumnIndexOrThrow(COLUMN_LOGIN))
                val avatarUrl = getString(getColumnIndexOrThrow(COLUMN_AVATAR_URL))
                val htmlUrl = getString(getColumnIndexOrThrow(COLUMN_HTML_URL))
                listUserFav.add(UserFavorite(id, login, avatarUrl, htmlUrl))
            }
        }
        return listUserFav
    }

}