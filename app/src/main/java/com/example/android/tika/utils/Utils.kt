package com.example.android.tika.utils

import com.example.android.tika.data.models.Task
import java.text.SimpleDateFormat
import java.util.*


/**
 * Computes the total number of comments in tasks
 * @param tasks scheduled tasks per day
 * @return number of comments from friends for the daily tasks
 */
fun totalNumberOfComments(tasks: List<Task>): Int {
    var total = 0
    tasks.forEach {
        total = total.plus(it.comments.size)
    }
    return total
}

fun formatDate(date: Date): String {
    val pattern = "YY . MM . dd"
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(date)
}

/**
 * Transforms the tasks into a flat list containing a
 * task title and all comments per task
 * @param tasks the list of task
 * @return a list of titles and comments
 */
fun flatMapTaskObject(tasks: List<Task>): MutableList<Any> {
    val items = mutableListOf<Any>()
    tasks.forEach { task->
        items.add(task.title)
        task.comments.forEach { comment ->
            items.add(comment)
        }
    }

    return items
}