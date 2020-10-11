package com.example.android.tika.data.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation

data class TaskWithComments(
    @Embedded val task: Task,
    @Relation(
        parentColumn = "taskId",
        entityColumn = "taskId"
    )
    val comments: List<Comment>
)

data class ColleagueWithComments(
    @Embedded val colleague: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "authorId"
    )
    val comments: List<Comment>
)

data class TaskWithSupport(
    @Embedded val task: Task,
    @Relation(
        parentColumn = "taskId",
        entityColumn = "userId",
        associateBy = Junction(TaskSupportCrossRef::class)
    )
    val colleagues: List<User>
)

data class ColleagueWithTasks(
    @Embedded val support: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "taskId",
        associateBy = Junction(TaskSupportCrossRef::class)
    )
    val tasks: List<Task>
)

data class ActivityWithTasks(
    @Embedded val activity: Activity,
    @Relation(
        parentColumn = "activityId",
        entityColumn = "activityId"
    )
    val tasks: List<Task>
)
@Entity(primaryKeys = ["userId", "taskId"])
data class TaskSupportCrossRef(
    val userId: Long = 0L,
    val taskId: Long
)

