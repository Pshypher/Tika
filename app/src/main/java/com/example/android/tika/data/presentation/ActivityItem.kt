package com.example.android.tika.data.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.tika.data.database.Task
import com.example.android.tika.data.database.TaskDao
import com.example.android.tika.data.database.TaskDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.combine
import java.text.SimpleDateFormat
import java.util.*

data class ActivityItem (
    val activityId: Long,
    val date: Date,
    val tasks: List<Task> = listOf(),
    var expanded: Boolean = false
)

/**
 * Formats the [Date] into a user-friendly string displayed in the activity panel
 * @param date [Date] object to be formatted
 * @ return a user friendly representation of a [Date]
 */
fun ActivityItem.formatDate(): String {
    val pattern = "YY . MM . dd"
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(date)
}

/**
 * Sums up the total number of completed tasks
 * @param tasks the list of task.db
 * @return total number of completed tasks
 */
fun ActivityItem.numberOfTasksCompleted(): Int {
    var total = 0
    tasks.forEach { task ->
        total = if (task.completed) total.plus(1) else total
    }

    return total
}

/**
 * Computes the total number of comments in tasks
 * @param tasks scheduled tasks per day
 * @return number of comments from friends for the daily tasks
 */
suspend fun ActivityItem.totalNumberOfComments(context: Context): Int {
    var total = 0
    val taskDao = TaskDatabase.getInstance(context.applicationContext).taskDao
    tasks.forEach { task ->
        val taskWithComments = taskDao.getTaskWithComment(task.taskId)
        total = total.plus(taskWithComments.comments.size)
    }

    return total
}