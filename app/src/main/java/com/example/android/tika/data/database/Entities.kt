package com.example.android.tika.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Activity(
    @PrimaryKey(autoGenerate = true) var activityId: Long = 0L,

    var date: Date
)

@Entity
data class Comment (
    @PrimaryKey(autoGenerate = true) var commentId: Long = 0L,

    var message: String,

    var taskId: Long,

    var authorId: Long
)

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) var taskId: Long = 0L,

    var title: String,

    var description: String?,

    var startTime: Date,

    var endTime: Date,

    var activityId: Long?,

    var completed: Boolean = false,
)

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) var userId: Long = 0L,

    var firstName: String?,

    var lastName: String?
)
