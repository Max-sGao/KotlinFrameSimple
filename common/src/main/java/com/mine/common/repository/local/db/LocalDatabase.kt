package com.mine.common.repository.local.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mine.common.repository.local.db.dao.UserDao
import com.mine.common.repository.local.db.entity.User
import com.mine.common.tools.AppUtils

@Database(entities = [User::class], version = LocalDatabase.DATABASE_VERSION, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {

    companion object {

        const val DATABASE_VERSION: Int = 1
        private const val DATABASE_NAME: String = "local_database"

        @Volatile
        private var instance: LocalDatabase? = null

        fun getInstance(): LocalDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    AppUtils.getApplication(),
                    LocalDatabase::class.java,
                    DATABASE_NAME
                ).build().also { instance = it }
            }
        }
    }

    abstract fun getUserDao(): UserDao
}