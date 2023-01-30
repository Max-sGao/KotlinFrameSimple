package com.mine.common.repository.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    val account: String,

    val token: String,
    @ColumnInfo(name = "user_id")
    val userId: String
) {
    @PrimaryKey(autoGenerate = true)
    var _id: Int = 0
}