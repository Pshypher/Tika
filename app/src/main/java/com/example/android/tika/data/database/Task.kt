package com.example.android.tika.data.database

import androidx.room.*
import java.util.Date

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true) var taskId: Long = 0L,

    var title: String?,

    var description: String?,

    var startTime: Date,

    var endTime: Date,

    var userTaskId: Long,

    var activityTaskId: Long
)

