package com.example.android.tika.utils

import android.content.Context
import com.example.android.tika.R
import com.example.android.tika.data.database.Comment

import com.example.android.tika.data.database.Task
import com.example.android.tika.data.database.TaskDatabase
import com.example.android.tika.data.database.User

/**
 * Transforms the tasks into a flat list containing a
 * task.db title and all comments per task.db
 * @param tasks the list of task.db
 * @return a list of titles and comments
 */
fun flatMapTaskObject(context: Context, tasks: List<Task>): MutableList<Any> {
    val items = mutableListOf<Any>()
    val dao = TaskDatabase.getInstance(context.applicationContext).taskDao
    tasks.forEach { task ->
        val taskWithComments =  dao.getTaskWithComment(task.taskId)
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
fun getAuthor(context: Context, comment: Comment): String {
    val dao = TaskDatabase.getInstance(context.applicationContext).taskDao
    val comments = dao.getCommentsWithAuthor(comment.email)
    val author = comments.colleague
    val name = author.firstName ?: author.lastName
    return name ?: context.getString(R.string.anonymous)
}