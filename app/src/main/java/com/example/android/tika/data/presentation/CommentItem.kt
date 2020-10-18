package com.example.android.tika.data.presentation

data class CommentItem(
    val commentId: Long,
    val message: String,
    var author: String?
)