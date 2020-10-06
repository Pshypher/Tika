package com.example.android.tika.data.database

import androidx.room.*

@Entity(tableName = "comment")
data class Comment (
    @PrimaryKey(autoGenerate = true) var commentId: Long = 0L,

    var message: String,

    var taskCommentId: Long
)