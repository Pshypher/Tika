package com.example.android.tika.data.presentation

import java.text.SimpleDateFormat
import java.util.*

data class TaskItem(
    val taskId: Long,
    val title: String,
    var date: String = "JAN 01"
)

fun TaskItem.formatDate(time: Long) {
    val pattern = "MMM dd"
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    date = formatter.format(Date(time)).toUpperCase(Locale.getDefault())
}

