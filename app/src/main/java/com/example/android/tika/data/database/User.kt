package com.example.android.tika.data.database

import androidx.room.*

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) var userId: Long = 0L,

    var firstName: String?,

    var LastName: String?,

    var commentId: Long = 0L
)

