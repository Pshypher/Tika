package com.example.android.tika.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "daily_activity")
data class Activity(
    @PrimaryKey(autoGenerate = true) var activityId: Long = 0L,

    var completed: Int,

    var total: Int,

    var date: Date
)