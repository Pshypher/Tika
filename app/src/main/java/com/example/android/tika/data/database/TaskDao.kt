package com.example.android.tika.data.database

import androidx.room.*

@Dao
interface TaskDao {

    @Insert
    fun addComment(comment: Comment)

    @Insert
    fun addTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @Query("SELECT * FROM task")
    fun getTasks(): List<Task>

    @Transaction
    @Query("SELECT * FROM comment")
    fun getCommentAndAuthor(): List<CommentAndAuthor>

    @Transaction
    @Query("SELECT * FROM task")
    fun getTasksWithComment(): List<TaskWithComments>

    @Transaction
    @Query("SELECT * FROM user")
    fun getUserTasks(): List<UserTasks>

    @Transaction
    @Query("SELECT * FROM user")
    fun getColleagueWithTasks(): List<ColleagueWithTasks>

    @Transaction
    @Query("SELECT * FROM task")
    fun getTaskWithSupport(): List<TaskWithSupport>
}
