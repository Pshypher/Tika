package com.example.android.tika.data.database

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class UserTasks(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userTaskId"
    )
    val tasks: List<Task>
)

data class TaskWithComments(
    @Embedded val task: Task,
    @Relation(
        parentColumn = "taskId",
        entityColumn = "taskCommentId"
    )
    val comments: List<Comment>
)

data class CommentAndAuthor(
    @Embedded val comment: Comment,
    @Relation(
        parentColumn = "commentId",
        entityColumn = "commentId"
    )
    val user: User
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
        entityColumn = "activityTaskId"
    )
    val tasks: List<Task>
)
