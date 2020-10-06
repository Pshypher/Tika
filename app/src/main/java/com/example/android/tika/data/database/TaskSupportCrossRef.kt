package com.example.android.tika.data.database

import androidx.room.Entity

@Entity(primaryKeys = ["user_id", "task_id"])
data class TaskSupportCrossRef(
    val userId: Long,
    val taskId: Long
)