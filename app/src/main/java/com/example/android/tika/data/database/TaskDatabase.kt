package com.example.android.tika.data.database

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.*

@Database(
    entities = [Comment::class, Task::class,
        User::class, TaskSupportCrossRef::class, Activity::class], version = 1, exportSchema = true
)
@TypeConverters(Converters::class)
abstract class TaskDatabase : RoomDatabase() {
    abstract val taskDao: TaskDao

    companion object {

        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getInstance(context: Context): TaskDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): TaskDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                TaskDatabase::class.java,
                "task_database"
            )
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        ioThread {
                            val dao = getInstance(context).taskDao
                            dao.insertUsers(getUsers())
                            dao.insertActivities(getActivities())
                            dao.insertTasks(getTasks())
                            dao.insertComments(getComments())
                            dao.insertCrossRef(getTaskSupportCrossRef())
                        }
                    }
                }).build()
        }

        fun getUsers(): List<User> {
            return listOf(
                User(firstName = "Mika", lastName = "Ghuanzhou"),
                User(firstName = "Temiloluwa", lastName = "Afolabi"),
                User(firstName = "Tunji", lastName = "Ojekunle"),
                User(firstName = "Saadiq", lastName = "Abdullahi")
            )
        }

        fun getActivities(): List<Activity> {
            return listOf(
                Activity(date = Date(1601593200000)),
                Activity(date = Date(1601506800000)),
                Activity(date = Date(1601420400000))
            )
        }

        fun getTasks(): List<Task> {
            return listOf(
                Task(
                    title = "Practice Karate", description = null, startTime = Date(1601451900000),
                    endTime = Date(1601470500000), completed = false, activityId = 2
                ),
                Task(
                    title = "Feng-Shui meditation",
                    description = null,
                    startTime = Date(1601476200000),
                    endTime = Date(1601485200000),
                    completed = false,
                    activityId = 2
                ),
                Task(
                    title = "Complete HTML & CSS in 30 days",
                    description = null,
                    startTime = Date(1601539200000),
                    endTime = Date(1601542800000),
                    completed = false,
                    activityId = 3
                ),
                Task(
                    title = "Reading 48 Laws of Power",
                    description = null,
                    startTime = Date(1601550000000),
                    endTime = Date(1601553600000),
                    completed = false,
                    activityId = 3
                ),
                Task(
                    title = "Practice Taekwando Poomse",
                    description = "I want to get stronger practicing my taekwando poomse.",
                    startTime = Date(1601493300000),
                    endTime = Date(1601497800000),
                    completed = false,
                    activityId = 3
                ),
                Task(
                    title = "Jog for miles",
                    description = "Trying to get my fitness levels through the roof",
                    startTime = Date(1601498700000),
                    endTime = Date(1601505225000),
                    completed = false,
                    activityId = 3
                )
            )
        }

        fun getComments(): List<Comment> {
            return listOf(
                Comment(message = "Well Done!", taskId = 2, authorId = 1),
                Comment(message = "go Jimi go", taskId = 2, authorId = 2),
                Comment(message = "Did you close the last comment tag?", taskId = 1, authorId = 3),
                Comment(
                    message = "I just finished chapter 12 myself, well done",
                    taskId = 4,
                    authorId = 4
                )
            )
        }

        fun getTaskSupportCrossRef(): List<TaskSupportCrossRef> {
            return listOf(
                TaskSupportCrossRef(2, 1),
                TaskSupportCrossRef(3, 1),
                TaskSupportCrossRef(1, 2),
                TaskSupportCrossRef(1, 3),
                TaskSupportCrossRef(2, 3),
                TaskSupportCrossRef(3, 3),
                TaskSupportCrossRef(4, 4),
                TaskSupportCrossRef(1, 5)
            )
        }
    }
}