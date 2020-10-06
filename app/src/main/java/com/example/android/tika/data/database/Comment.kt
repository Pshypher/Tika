package com.example.android.tika.data.database

import androidx.room.*

@Entity(tableName = "comment")
data class Comment (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "comment_id") var commentId: Long = 0L,

    var message: String,

    @ColumnInfo(name="task_column_id")
    var taskCommentId: Long
)