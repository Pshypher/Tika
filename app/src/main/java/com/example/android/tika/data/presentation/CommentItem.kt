package com.example.android.tika.data.presentation

import com.example.android.tika.data.database.Comment

data class CommentItem(
    val comment: Comment,
    var author: String?
)