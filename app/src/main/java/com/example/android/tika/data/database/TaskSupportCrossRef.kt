package com.example.android.tika.data.database

import androidx.room.Entity

@Entity(primaryKeys = ["userId", "taskId"])
data class TaskSupportCrossRef(
    val userId: Long,
    val taskId: Long
)