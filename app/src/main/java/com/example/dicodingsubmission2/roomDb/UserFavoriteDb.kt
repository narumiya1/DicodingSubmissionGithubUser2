package com.example.dicodingsubmission2.roomDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserFavorite::class], version = 1, exportSchema = false)
abstract class UserFavoriteDb : RoomDatabase() {

    abstract fun usersFav(): UserFavoriteDaoQuery

    companion object {
        private var INSTANCE: UserFavoriteDb? = null
        fun getAppDatabase(context: Context): UserFavoriteDb? {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    UserFavoriteDb::class.java, "room-kotlin-database"
                ).build()
            }
            return INSTANCE
        }

    }
}