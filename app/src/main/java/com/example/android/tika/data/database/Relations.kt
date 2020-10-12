package com.example.android.tika.data.database

import androidx.room.Embedded
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
        parentColumn = "email",
        entityColumn = "email"
    )
    val comments: List<Comment>
)

data class TaskWithSupport(
    @Embedded val task: Task,
    @Relation(
        parentColumn = "taskId",
        entityColumn = "email",
        associateBy = Junction(TaskSupportCrossRef::class)
    )
    val colleagues: List<User>
)

data class ColleagueWithTasks(
    @Embedded val support: User,
    @Relation(
        parentColumn = "email",
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

