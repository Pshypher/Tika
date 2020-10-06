package com.example.android.tika.data.database

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class UserTasks(
    @Embedded val user: User,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "user_task_id"
    )
    val tasks: List<Task>
)

data class TaskWithComments(
    @Embedded val task: Task,
    @Relation(
        parentColumn = "task_id",
        entityColumn = "task_comment_id"
    )
    val comments: List<Comment>
)

data class CommentAndAuthor(
    @Embedded val comment: Comment,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "author_comment_id"
    )
    val user: User
)

data class TaskWithSupport(
    @Embedded val task: Task,
    @Relation(
        parentColumn = "task_id",
        entityColumn = "user_id",
        associateBy = Junction(TaskSupportCrossRef::class)
    )
    val colleagues: List<User>
)

data class ColleagueWithTasks(
    @Embedded val support: User,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "task_id",
        associateBy = Junction(TaskSupportCrossRef::class)
    )
    val tasks: List<Task>
)