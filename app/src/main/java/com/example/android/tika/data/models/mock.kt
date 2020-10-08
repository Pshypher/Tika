package com.example.android.tika.data.models

import java.text.SimpleDateFormat
import java.util.*

data class Activity(
    val completed: Int,
    val total: Int,
    val date: Date,
    val tasks: List<Task> = listOf(),
    var expanded: Boolean = false
)

data class Task(
    val title: String,
    val description: String,
    val startTime: Long,
    val endTime: Long,
    val users: List<User> = listOf(),
    val comments: List<Comment> = listOf(),
    var date: String? = null
)

fun Task.formatDate(time: Long): String {
    val pattern = "MMM dd"
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(Date(time)).toUpperCase(Locale.getDefault())
}

data class User(
    val mobileNumber: Int,
    val firstName: String,
    val lastName: String,
    var tasks: List<Task> = listOf()
)

data class Comment(
    val author: User,
    val message: String
)