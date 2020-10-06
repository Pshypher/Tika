package com.example.android.tika.data.database

import androidx.room.*

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id") var userId: Long = 0L,

    @ColumnInfo(name = "first_name")
    var firstName: String?,

    @ColumnInfo(name = "last_name")
    var LastName: String?,

    @ColumnInfo(name = "author_comment_id")
    var authorId: Long = 0L
)

