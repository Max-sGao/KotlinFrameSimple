package com.mine.common.repository.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mine.common.repository.local.db.entity.User

@Dao
interface UserDao : IDao{

    @Insert
    fun insert(user: User)

    @Query("SELECT * FROM User")
    fun query(): List<User>

    @Update
    fun update(user: User)

    @Query("DELETE FROM User")
    fun delete()

}