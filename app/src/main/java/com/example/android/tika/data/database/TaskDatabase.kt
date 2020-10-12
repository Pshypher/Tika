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
                })
                .fallbackToDestructiveMigration()
                .build()
        }

        fun getUsers(): List<User> {
            return listOf(
                User(email = "mika_g@baidu.com", firstName = "Mika", lastName = "Ghuanzhou"),
                User(email = "anonymous@tika.tech",  firstName = "Temiloluwa", lastName = "Afolabi"),
                User(email = "ojekunleadetunji@gmail.com",  firstName = "Tunji", lastName = "Ojekunle"),
                User(email = "abaraybone@tinariwen.ml", firstName = "Ibrahim", lastName = "Ag-Alhabib"),
                User(email = "john_doe@gmail.com", firstName = "John", lastName = "Doe")
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
                Comment(message = "Well Done!", taskId = 2, email = "mika_g@baidu.com"),
                Comment(message = "go Jimi go", taskId = 2, email = "anonymous@tika.tech"),
                Comment(message = "Did you close the last comment tag?", taskId = 1,
                    email = "ojekunleadetunji@gmail.com"),
                Comment(
                    message = "I just finished chapter 12 myself, well done",
                    taskId = 4,
                    email = "abaraybone@tinariwen.ml"
                ),
                Comment(
                    message = "Get rid of that extra pound",
                    taskId = 6,
                    email = "john_doe@gmail.com"
                )
            )
        }

        fun getTaskSupportCrossRef(): List<TaskSupportCrossRef> {
            return listOf(
                TaskSupportCrossRef("anonymous@tika.tech", 1),
                TaskSupportCrossRef("ojekunleadetunji@gmail.com", 1),
                TaskSupportCrossRef("mika_g@baidu.com", 2),
                TaskSupportCrossRef("mika_g@baidu.com", 3),
                TaskSupportCrossRef("anonymous@tika.tech", 3),
                TaskSupportCrossRef("ojekunleadetunji@gmail.com", 3),
                TaskSupportCrossRef("abaraybone@tinariwen.ml", 4),
                TaskSupportCrossRef("mika_g@baidu.com", 5),
                TaskSupportCrossRef("john_doe@gmail.com", 6)
            )
        }
    }
}