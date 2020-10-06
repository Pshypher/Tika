package com.example.android.tika.data.database

import androidx.room.*
import java.util.*

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id") var taskId: Long = 0L,

    var title: String?,

    var description: String?,

    var date: Date,

    @ColumnInfo(name = "start_time")
    var startTime: Long,

    @ColumnInfo(name= "end_time")
    var endTime: Long,

    @ColumnInfo(name = "user_task_id")
    var userTaskId: Long
)

