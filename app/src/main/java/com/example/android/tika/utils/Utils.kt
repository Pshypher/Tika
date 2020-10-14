package com.example.android.tika.utils

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.tika.R
import com.example.android.tika.data.database.Comment

import com.example.android.tika.data.database.Task
import com.example.android.tika.data.database.TaskDatabase
import com.example.android.tika.data.database.User
import kotlinx.coroutines.CoroutineScope

/**
 * Transforms the tasks into a flat list containing a
 * task.db title and all comments per task.db
 * @param context resources, styles, themes ... are provided by the context
 * @param tasks the list of task.db
 * @return a livedata object of a list of titles and comments
 */
suspend fun flatMapTaskObject(context: Context, tasks: List<Task>): MutableList<Any> {
    val taskDao = TaskDatabase.getInstance(context.applicationContext).taskDao
    val items: MutableList<Any> = mutableListOf()
    tasks.forEach { task ->
        val taskWithComments = taskDao.getTaskWithComment(task.taskId)
        items.add(task.title)
        taskWithComments.comments.forEach { comment ->
            items.add(comment)
        }
    }

    return items
}

/**
 * Gets the name of a [User] who authored the passed in [Comment]
 * @param context application context to access the [TaskDatabase]
 * @param comment remark made by a User
 * @return name of the author
 */
suspend fun getAuthor(context: Context, comment: Comment): String {
    val dao = TaskDatabase.getInstance(context.applicationContext).taskDao
    val comments = dao.getCommentsWithAuthor(comment.email)
    val author = comments.colleague
    return author.firstName ?: author.lastName ?: context.getString(R.string.anonymous)
}