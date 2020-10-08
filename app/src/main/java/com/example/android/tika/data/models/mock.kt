package com.example.android.tika.data.models

import java.util.*

data class Activity(
    val completed: Int,
    val total: Int,
    val date: Date,
    val tasks: List<Task> = listOf()
)

data class Task(
    val title: String,
    val description: String,
    val startTime: Date,
    val endTime: Date,
    val users: List<User> = listOf(),
    val comments: List<Comment> = listOf()
)

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