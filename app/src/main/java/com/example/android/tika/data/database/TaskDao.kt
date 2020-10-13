package com.example.android.tika.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao {

    @Insert
    fun insertUser(user: User)

    @Insert
    fun insertComment(comment: Comment)

    @Insert
    fun insertTask(task: Task)

    @Insert
    fun insertUsers(users: List<User>)

    @Insert
    fun insertActivities(activities: List<Activity>)

    @Insert
    fun insertComments(comments: List<Comment>)

    @Insert
    fun insertTasks(tasks: List<Task>)

    @Insert
    fun insertCrossRef(refs: List<TaskSupportCrossRef>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTask(task: Task)

    @Query("SELECT * FROM Task")
    fun getTasks(): List<Task>

    @Transaction
    @Query("SELECT * FROM User WHERE email LIKE :key")
    fun getCommentsWithAuthor(key: String): ColleagueWithComments

    @Transaction
    @Query("SELECT * FROM Task WHERE taskId = :key")
    fun getTaskWithComment(key: Long): TaskWithComments

    @Transaction
    @Query("SELECT * FROM User")
    fun getColleagueWithTasks(): List<ColleagueWithTasks>

    @Transaction
    @Query("SELECT * FROM Task")
    fun getTaskWithSupport(): List<TaskWithSupport>

    @Transaction
    @Query("SELECT * FROM Activity")
    fun getActivities(): List<ActivityWithTasks>
}
