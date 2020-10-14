package com.example.android.tika.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao {

    @Insert
    suspend fun insertUser(user: User)

    @Insert
    suspend fun insertComment(comment: Comment)

    @Insert
    suspend fun insertTask(task: Task)

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
    suspend fun updateTask(task: Task)

    @Query("SELECT * FROM Task")
    fun getTasks(): LiveData<List<Task>>

    @Transaction
    @Query("SELECT * FROM User WHERE email LIKE :key")
    fun getCommentsWithAuthor(key: String): ColleagueWithComments

    @Transaction
    @Query("SELECT * FROM Task WHERE taskId = :key")
    fun getTaskWithComment(key: Long): TaskWithComments

    @Transaction
    @Query("SELECT * FROM User")
    fun getColleagueWithTasks(): LiveData<List<ColleagueWithTasks>>

    @Transaction
    @Query("SELECT * FROM Task")
    fun getTaskWithSupport(): LiveData<List<TaskWithSupport>>

    @Transaction
    @Query("SELECT * FROM Activity")
    fun getActivities(): LiveData<List<ActivityWithTasks>>
}
