package com.example.todolist

import java.sql.Blob

data class Task(
    val id: Long,
    var title: String,
    var done: Boolean
)
