package com.example.dicodingsubmission2.roomDb

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserFavoriteDaoQuery {

    @Insert
    fun insert(user: UserFavorite)

    @Update
    fun update(user: UserFavorite)

    @Update
    fun delete(user: UserFavorite)

    @Query("DELETE FROM USER_FAVS WHERE id==:id")
    fun deleteUserWithId(id: Int): Int

    @Query("SELECT * FROM USER_FAVS")
    fun getAllUsers(): List<UserFavorite>

    @Query("SELECT * FROM USER_FAVS")
    fun getAllFavoriteUsers(): Cursor?

    @Query("SELECT * FROM USER_FAVS WHERE id==:id")
    fun getUserWithId(id: Int): List<UserFavorite>

    @Query("SELECT * FROM USER_FAVS WHERE id==:id")
    fun getFavUserWithId(id: Int): Cursor?
}